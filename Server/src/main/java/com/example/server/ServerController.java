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
   ArrayList<Network> networkArrayList=new ArrayList<>();
   int i=0;



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
                            networkArrayList.add(network);
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    i++;
                                    circle.setFill(Color.GREEN);
                                    label.setText("server connected to "+i +"client ....");
                                }
                            });
                        }




                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();



            }
        });
    }
}