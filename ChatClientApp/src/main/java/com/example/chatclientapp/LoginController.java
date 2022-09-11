package com.example.chatclientapp;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
@FXML
    TextField username,password;
@FXML
    Button button,random;

Socket socket;
    Client client;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    socket=new Socket("localhost",1234);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                client=new Client(socket);
                    client.sendMessage(username.getText());
            }
        });
        random.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                client.sendMessage(username.getText());
            }
        });
    }
}
