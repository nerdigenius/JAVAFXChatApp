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

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ServerController implements Initializable {
   @FXML
   Label label;
   @FXML
    Button button;
   @FXML
    Circle circle;

   Network network=null;

   ArrayList<ClientHandler> clientHandlerArrayList = new ArrayList<>();



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
                            network = new Network(new ServerSocket(1234));
                            clientHandlerArrayList.add(new ClientHandler(network.getSocket()));
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {

                                    circle.setFill(Color.GREEN);
                                    label.setText("server connected to "+clientHandlerArrayList.size() +"client ....");
                                }
                            });
                        }




                    } catch (IOException e) {
                        e.printStackTrace();
                        button.setDisable(false);
                    }
                }).start();



            }
        });
    }
}