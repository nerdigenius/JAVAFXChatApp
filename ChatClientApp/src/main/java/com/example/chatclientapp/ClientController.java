package com.example.chatclientapp;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    @FXML
    private Button SendButton;
    @FXML
    private TextField tfMessages;
    @FXML
    private VBox messages,clientList;
    @FXML
    private ScrollPane scroll;
    @FXML
    private Label selectedUserLabel,MessengerHeader;

    private NetworkUtil client=null;

    Stage stage=null;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {



        messages.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                scroll.setVvalue((Double)t1);
            }
        });

        messages.setBackground(new Background(new BackgroundFill(null,null,null)));
        scroll.setFitToWidth(true);





        SendButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String sendingMessage=tfMessages.getText();
                stage = (Stage) tfMessages.getScene().getWindow();
                UserData userData=(UserData) stage.getUserData();
                client= userData.getNetworkUtil();
                if(!sendingMessage.isEmpty()&&client!=null&&!selectedUserLabel.getText().isEmpty()){
                    HBox hBox = new HBox();
                    hBox.setBackground(new Background(new BackgroundFill(null, CornerRadii.EMPTY, Insets.EMPTY)));
                    Text text = new Text(sendingMessage);
                    text.setFill(Color.WHITE);
                    text.setFont(new Font(13));
                    hBox.setAlignment(Pos.CENTER_RIGHT);
                    hBox.setPadding(new Insets(5));
                    TextFlow textFlow = new TextFlow(text);
                    textFlow.setPadding(new Insets(10));
                    textFlow.setBackground(new Background(new BackgroundFill(Color.GREEN,new CornerRadii(10), Insets.EMPTY)));
                    textFlow.setMaxWidth(200);
                    hBox.getChildren().add(textFlow);
                    messages.getChildren().add(hBox);


                    try {
                        Message sendMessage=new Message("MessageTo","MessageFrom",tfMessages.getText(),"Message");
                        client.write(selectedUserLabel.getText()+";"+userData.getUserName()+";"+tfMessages.getText()+";"+"Message");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    tfMessages.clear();
                }
            }
        });
        new Thread(()->{
            UserData userData = null;
            boolean connection=false;
            while (connection==false){
                try{
                    stage = (Stage) tfMessages.getScene().getWindow();
                    userData=(UserData) stage.getUserData();
                    client= userData.getNetworkUtil();
                    System.out.println("Client connection created");
                    connection=true;
                    MessengerHeader.setText(userData.getUserName());
                    Window window = stage;
                    window.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent windowEvent) {
                            try {
                                client.closeConnection();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                catch (Exception e){
                    e.printStackTrace();
                    System.out.println("Client not created");
                }
            }
            while (true){

                try {
                    String message=(String) client.read();
                    if(message.startsWith("list")){
                        updateUserOnlineList(message, userData.getUserName());
                    }
                    else{
                        newLabel(message,messages);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }


        }).start();
    }

    public static void newLabel(String clientMessage,VBox vBox){
        String[] messageArray=clientMessage.split(";");

            HBox hBox = new HBox();
            hBox.setBackground(new Background(new BackgroundFill(null, CornerRadii.EMPTY, Insets.EMPTY)));
            Text text = new Text(messageArray[2]);
            text.setFill(Color.WHITE);
            text.setFont(new Font(13));
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setPadding(new Insets(5));
            TextFlow textFlow = new TextFlow(text);
            textFlow.setPadding(new Insets(10));
            textFlow.setBackground(new Background(new BackgroundFill(Color.DARKBLUE,new CornerRadii(10), Insets.EMPTY)));
            textFlow.setMaxWidth(200);
            hBox.getChildren().add(new Label(messageArray[1]));
            hBox.getChildren().add(textFlow);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    vBox.getChildren().add(hBox);
                }
            });

        }
        public void updateUserOnlineList(String list,String userName){
        String[] messageArray=list.split(";");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                clientList.getChildren().clear();
            }
        });
            for (String text:messageArray
            ) {
                if (!text.equals("list")&&!text.equals(userName)) {
                    Button button = new Button();
                    button.setId(text);
                    button.setMinWidth(150);
                    button.setText(text);
                    button.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            selectedUserLabel.setText(button.getId());
                        }
                    });
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            clientList.getChildren().add(button);
                        }
                    });


                }
            }
        }

    }
