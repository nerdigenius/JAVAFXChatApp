module com.example.chatclientapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.chatclientapp to javafx.fxml;
    exports com.example.chatclientapp;
}