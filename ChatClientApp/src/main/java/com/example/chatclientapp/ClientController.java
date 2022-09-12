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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

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
    private VBox messages;
    @FXML
    private ScrollPane scroll;

    private Client client=null;

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

                System.out.println(client);
                if(!sendingMessage.isEmpty()&&client!=null){
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


                    client.sendMessage(sendingMessage);
                    tfMessages.clear();
                }
            }
        });
        new Thread(()->{
            boolean connection=false;
            while (connection==false){
                try{
                    Thread.sleep(1000);
                    client = new Client(new Socket("localhost",1234));
                    System.out.println("Client connection created");
                    connection=true;
                }
                catch (Exception e){
                    e.printStackTrace();
                    System.out.println("Client not created");
                }
            }

            client.getServerMessages(messages);

        }).start();

    }

    public static void newLabel(String clientMessage,VBox vBox){
        HBox hBox = new HBox();
        hBox.setBackground(new Background(new BackgroundFill(null, CornerRadii.EMPTY, Insets.EMPTY)));
        Text text = new Text(clientMessage);
        text.setFill(Color.WHITE);
        text.setFont(new Font(13));
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5));
        TextFlow textFlow = new TextFlow(text);
        textFlow.setPadding(new Insets(10));
        textFlow.setBackground(new Background(new BackgroundFill(Color.DARKBLUE,new CornerRadii(10), Insets.EMPTY)));
        textFlow.setMaxWidth(200);
        hBox.getChildren().add(textFlow);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vBox.getChildren().add(hBox);
            }
        });
    }
}