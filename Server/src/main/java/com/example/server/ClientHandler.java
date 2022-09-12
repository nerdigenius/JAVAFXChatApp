package com.example.server;

import javafx.scene.control.Label;
import javafx.scene.shape.Circle;

import java.io.*;
import java.net.Socket;

import static com.example.server.ServerController.*;


public class ClientHandler implements Runnable{
    private Socket client;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    boolean authentic=false;
    Label label;
    Circle circle;
    String username;

    public ClientHandler(Socket client,Label label, Circle circle) throws IOException {
        this.client = client;
        this.bufferedReader=new BufferedReader(new InputStreamReader(client.getInputStream()));
        this.bufferedWriter=new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        this.label=label;
        this.circle=circle;
    }



    public void closeServer(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        try{
            if (bufferedReader!= null){
                bufferedReader.close();
            }
            if (bufferedWriter!= null){
                bufferedWriter.close();
            }
            if (socket!= null){
                socket.close();
            }
        }
        catch (Exception e){
            System.out.println("Server Close Error");
            e.printStackTrace();
        }
    }

    boolean authenticate(String incomingMsg){
        String[] incomingArray=incomingMsg.split(";");
        BufferedReader reader;
       try {
            reader = new BufferedReader(new FileReader("src/main/resources/com/example/server/password"));
            String line = reader.readLine();

            while (line != null) {
                String[] arr=line.split(";");
                if(incomingArray[1].equals(arr[0])&&incomingArray[2].equals(arr[1])){
                    username=incomingArray[1];
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
    public void sendMessage(String sendingMessage) {
        try {
            bufferedWriter.write(sendingMessage);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("Error Sending sendingMessage");
            closeServer(client,bufferedReader,bufferedWriter);
            Thread.currentThread().interrupt();
        }
    }

    public Socket getClient() {
        return client;
    }

    public boolean isAuthentic() {
        return authentic;
    }

    public void getAllusers(){
        String userNames="";
        for (ClientHandler client:clientHandlerArrayList
             ) {
            if(!client.getUsername().equals(username)){
                userNames=userNames+client.getUsername()+";";
            }
        }
        sendMessage(userNames);
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void run() {
        String incomingMsg=null;
        try {
            while (client.isConnected()){
                this.bufferedReader=new BufferedReader(new InputStreamReader(client.getInputStream()));
                this.bufferedWriter=new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                incomingMsg=bufferedReader.readLine();
                System.out.println(incomingMsg);

                    if(authentic==false){
                        if(authenticate(incomingMsg)) {
                            sendMessage("authenticated");
                            System.out.println("message Sent");
                            authentic=true;
                            addClient(this);
                            updateLabel(label,circle);
                        }
                        else{
                            closeServer(client,bufferedReader,bufferedWriter);
                        }
                    }
                    else{
                        getAllusers();

                    }

            }


        }
        catch (Exception e){
            System.out.println("Server Error");
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        finally {
            closeServer(client,bufferedReader,bufferedWriter);
        }
    }


}
