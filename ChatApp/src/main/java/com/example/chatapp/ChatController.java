package com.example.chatapp;

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
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.net.ServerSocket;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatController implements Initializable {
    @FXML
    private Button SendButton;
    @FXML
    private TextField tfMessages;
    @FXML
    private VBox messages;
    @FXML
    private ScrollPane scroll;

    private Server server=null;



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
                if(!sendingMessage.isEmpty()&&server!=null){
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


                    server.sendMessage(sendingMessage);
                    tfMessages.clear();
                }
            }
        });

        new Thread(()->{
                try{
                    server = new Server(new ServerSocket(1234));
                    System.out.println("Server  created");
                }
                catch (Exception e){
                    e.printStackTrace();
                    System.out.println("Server not created");
                }

                server.getClientMessage(messages);
            }
        ).start();




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

        Platform.runLater(() -> vBox.getChildren().add(hBox));
    }
}