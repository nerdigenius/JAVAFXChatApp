package com.example.server;

import javafx.scene.layout.VBox;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Network {

    private ServerSocket serverSocket;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    private static ArrayList<Network> clients = new ArrayList<Network>();

    public Network(ServerSocket serverSocket){
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
        finally {
            closeServer(socket,bufferedReader,bufferedWriter);
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




    public BufferedReader getBufferedReader() {
        return bufferedReader;
    }

    public BufferedWriter getBufferedWriter() {
        return bufferedWriter;
    }

    public Socket getSocket() {
        return socket;
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
