package com.example.chatclientapp;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
@FXML
    TextField username,password;
@FXML
    Button button,random;
@FXML
    Label error;

Socket socket;
    Client client;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String authentication="";
                try {
                    socket=new Socket("localhost",1234);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                client=new Client(socket);
                    client.sendMessage("authentication;"+username.getText()+";"+password.getText());
                try {
                    authentication=client.getMessage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(authentication.equals("authenticated")){
                    Stage stage;
                    Parent root=null;
                    stage = (Stage) button.getScene().getWindow();
                    try {
                        root = FXMLLoader.load(getClass().getResource("Client-view.fxml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Scene scene = new Scene(root);
                    stage.setUserData(client);
                    stage.setScene(scene);
                    stage.show();

                }
                else{
                    error.setTextFill(Color.RED);
                    error.setText("There was an error try again!!!!");
                    client.closeConnection();
                }
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
