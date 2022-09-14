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

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;


public class SignUpController implements Initializable {
    @FXML
    TextField username,password;
    @FXML
    Button SignUp;
    @FXML
    Label error;
    Socket socket;
    NetworkUtil networkUtil;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SignUp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    socket=new Socket("localhost",1234);
                    networkUtil=new NetworkUtil(socket);
                    networkUtil.write("SignUp;"+username.getText()+";"+password.getText());
                    String returnedMessage=(String) networkUtil.read();
                    if(returnedMessage.equals("Success")){
                        Stage stage;
                        Parent root=null;
                        stage = (Stage) SignUp.getScene().getWindow();
                        try {
                            root = FXMLLoader.load(getClass().getResource("Login-view.fxml"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                        networkUtil.closeConnection();
                    }
                    else{
                        error.setTextFill(Color.RED);
                        error.setText("There was an error try again!!!!");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    error.setTextFill(Color.RED);
                    error.setText("There was an error try again!!!!");
                }


            }
        });
    }
}
