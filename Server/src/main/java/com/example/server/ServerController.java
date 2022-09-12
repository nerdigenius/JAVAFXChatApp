package com.example.server;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static java.lang.Thread.sleep;

public class ServerController implements Initializable {
   @FXML
   Label label;
   @FXML
    Button button;
   @FXML
    Circle circle;


   static ArrayList<ClientHandler> clientHandlerArrayList = new ArrayList<>();
    ServerSocket serverSocket;

    {
        try {
            serverSocket = new ServerSocket(1234);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                label.setText("Waiting for a client to connect to server ....");
                button.setDisable(true);
                new Thread(()->{
                    try {
                        while (true){

                            Socket socket=serverSocket.accept();

                            ClientHandler clientHandler=new ClientHandler(socket,label,circle);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    clientHandler.run();
                                }
                            }).start();

                        }




                    } catch (IOException e) {
                        e.printStackTrace();
                        button.setDisable(false);
                    }
                }).start();
                Window stage = label.getScene().getWindow();
                stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent windowEvent) {
                        try {
                            serverSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });


            }
        });
    }
    public static void addClient(ClientHandler clientHandler){
        clientHandlerArrayList.add(clientHandler);
        System.out.println("client array size: "+clientHandlerArrayList.size());
    }

    public static void updateLabel(Label label,Circle circle){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                circle.setFill(Color.GREEN);

                label.setText("server connected to "+clientHandlerArrayList.size() +"client ....");
            }
        });
    }
}