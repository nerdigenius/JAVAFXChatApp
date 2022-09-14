package com.example.chatclientapp;

import javafx.scene.layout.VBox;

import java.io.IOException;

public class ReadThread implements Runnable{
    private Thread thr;
    private NetworkUtil networkUtil;
    private VBox vBox;

    public ReadThread(NetworkUtil networkUtil,VBox vBox) {
        this.networkUtil = networkUtil;
        this.vBox=vBox;
        this.thr = new Thread(this);
        thr.start();
    }

    public void run() {
        try {
            while (true) {
                Message message = (Message) networkUtil.read();
                ClientController.newLabel(message.getTextMessage(),vBox);

            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                networkUtil.closeConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
