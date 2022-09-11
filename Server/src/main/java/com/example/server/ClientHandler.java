package com.example.server;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private Socket client;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public ClientHandler(Socket client) throws IOException {
        this.client = client;
        this.bufferedReader=new BufferedReader(new InputStreamReader(client.getInputStream()));
        this.bufferedWriter=new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
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

    @Override
    public void run() {
        try {
            while (client.isConnected()){
                this.bufferedReader=new BufferedReader(new InputStreamReader(client.getInputStream()));
                this.bufferedWriter=new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                System.out.println(bufferedReader.readLine());
            }


        }
        catch (Exception e){
            System.out.println("Server Error");
            e.printStackTrace();
        }
        finally {
            closeServer(client,bufferedReader,bufferedWriter);
        }
    }
}
