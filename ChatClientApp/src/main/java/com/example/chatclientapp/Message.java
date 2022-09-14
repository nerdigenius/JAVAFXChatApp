package com.example.chatclientapp;

import java.io.Serializable;

public class Message implements Serializable {
    String MessageTo,MessageFrom,TextMessage,Type;

    public String getMessageTo() {
        return MessageTo;
    }

    public String getMessageFrom() {
        return MessageFrom;
    }

    public String getTextMessage() {
        return TextMessage;
    }

    public String getType() {
        return Type;
    }

    public void setMessageTo(String messageTo) {
        MessageTo = messageTo;
    }

    public void setMessageFrom(String messageFrom) {
        MessageFrom = messageFrom;
    }

    public void setTextMessage(String textMessage) {
        TextMessage = textMessage;
    }

    public void setType(String type) {
        Type = type;
    }

    public Message(String messageTo, String messageFrom, String textMessage, String type) {
        MessageTo = messageTo;
        MessageFrom = messageFrom;
        TextMessage = textMessage;
        Type=type;
    }
}
