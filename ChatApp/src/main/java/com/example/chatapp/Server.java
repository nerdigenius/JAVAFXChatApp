package com.example.chatapp;

import javafx.scene.layout.VBox;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public Server(ServerSocket serverSocket){
        try {
            this.serverSocket = serverSocket;
            this.socket = serverSocket.accept();
            this.bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }
        catch (Exception e){
            System.out.println("Server Error");
            e.printStackTrace();
        }

    }

    public void sendMessage(String message){
        try {
            bufferedWriter.write(message);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("Error Sending message");
            closeServer(socket,bufferedReader,bufferedWriter);
        }
    }

    public void getClientMessage(VBox vBox){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(socket.isConnected()){
                    String clientMessage;
                    try {
                        clientMessage = bufferedReader.readLine();
                        ChatController.newLabel(clientMessage,vBox);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Error receiving messages from client");
                        closeServer(socket,bufferedReader,bufferedWriter);
                        break;
                    }

                }
            }
        }).start();
    }

    public Socket getSocket() {
        return socket;
    }

    public BufferedReader getBufferedReader() {
        return bufferedReader;
    }

    public BufferedWriter getBufferedWriter() {
        return bufferedWriter;
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
}
