package com.example.chatclientapp;

import javafx.scene.layout.VBox;

import java.io.*;
import java.net.Socket;

public class Client {

    private Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;

    public Client(Socket localhost) {
        try{
            this.socket=localhost;
            this.bufferedReader= new BufferedReader(new InputStreamReader(localhost.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(localhost.getOutputStream()));
        }
        catch (Exception e){
            e.printStackTrace();
            closeConnection(localhost,bufferedWriter,bufferedReader);
        }
    }

    private void closeConnection(Socket localhost, BufferedWriter bufferedWriter, BufferedReader bufferedReader) {
        try{
            if (bufferedReader != null){
                bufferedReader.close();
            }
            if (bufferedWriter != null){
                bufferedWriter.close();
            }
            if (localhost!= null){
                localhost.close();
            }
        }
        catch (Exception e){
            System.out.println("Client Close Error");
            e.printStackTrace();
        }
    }
    public void closeConnection() {
        try{
            if (bufferedReader != null){
                bufferedReader.close();
            }
            if (bufferedWriter != null){
                bufferedWriter.close();
            }
            if (socket!= null){
                socket.close();
            }
        }
        catch (Exception e){
            System.out.println("Client Close Error");
            e.printStackTrace();
        }
    }

    public void getServerMessages(VBox messages) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(socket.isConnected()){
                    String clientMessage;
                    try {
                        clientMessage = bufferedReader.readLine();
                        ClientController.newLabel(clientMessage,messages);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Error receiving messages from client");
                        closeConnection(socket,bufferedWriter,bufferedReader);
                        break;
                    }

                }
            }
        }).start();
    }

    public String getMessage() throws IOException {
        String clientMessage="none";
        while (socket.isConnected()){
            clientMessage = bufferedReader.readLine();
            return clientMessage;
        }
        return clientMessage;
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
           }
    }
}
