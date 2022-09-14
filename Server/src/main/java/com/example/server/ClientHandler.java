package com.example.server;

import javafx.scene.control.Label;
import javafx.scene.shape.Circle;

import java.io.*;


public class ClientHandler implements Runnable{
    private NetworkUtil networkUtil;
    Label label;
    Circle circle;
    String username;

    public ClientHandler(NetworkUtil networkUtil, Label label, Circle circle) {
        this.networkUtil = networkUtil;
        this.label = label;
        this.circle = circle;
    }


    boolean authenticate(String incomingMsg){
        String[] incomingArray=incomingMsg.split(";");
        BufferedReader reader;
       try {
            reader = new BufferedReader(new FileReader("src/main/resources/com/example/server/password"));
            String line = reader.readLine();

            while (line != null) {
                String[] arr=line.split(";");
                if(incomingArray[0].equals(arr[0])&&incomingArray[1].equals(arr[1])){
                    username=incomingArray[0];
                    return true;
                }
                System.out.println(arr[0]);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void SignUp(String incomingMsg) throws IOException {
        String[] incomingArray=incomingMsg.split(";");
        BufferedWriter writer;
        writer=new BufferedWriter(new FileWriter("src/main/resources/com/example/server/password",true));
        writer.newLine();
        writer.write(incomingArray[1]+";"+incomingArray[2]);
        writer.close();
        networkUtil.write("Success");


    }



    public String getUsername() {
        return username;
    }
    public void writeMessage(Message message) throws IOException {
        networkUtil.write(message.getMessageTo()+";"+message.getMessageFrom()+";"+message.getTextMessage()+";"+message.getType());
    }
    public void writeMessage(String message) throws IOException {
        networkUtil.write(message);
    }

    @Override
    public void run() {
        boolean authenticated=false;
        while (true){
            if(authenticated==false){
                try {
                    String userInfo=(String) networkUtil.read();
                    if(userInfo.startsWith("SignUp")){
                        SignUp(userInfo);
                        break;
                    }
                    authenticated=authenticate(userInfo);
                    if(authenticated){
                        networkUtil.write("authenticated");
                        ServerController.addClient(this);
                    }
                    else {
                        networkUtil.closeConnection();
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    try {
                        networkUtil.closeConnection();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

            else {
                try {
                    String message = (String) networkUtil.read();
                    String[] incomingArray=message.split(";");
                    System.out.println(incomingArray[2]);
                    Message sendMessage = new Message(incomingArray[0],incomingArray[1], incomingArray[2], incomingArray[3]);
                    for (ClientHandler findClient:ServerController.clientHandlerArrayList
                         ) {
                        System.out.println(findClient.getUsername());
                        if(findClient.getUsername().equals(sendMessage.MessageTo)){
                            findClient.writeMessage(sendMessage);
                        }
                    }

                }
                catch (Exception e){
                    e.printStackTrace();
                    ServerController.removeClient(this.username);
                    break;

                }

            }

        }

    }
}
