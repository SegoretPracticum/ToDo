package com.example.myapplication.appmessages;

public enum ErrorMessage {
    CONNECTION_ERROR ("There isn't internet connection"),
    SERVER_ERROR("Failed to connect to the server"),
    EMPTY_TEXT("Please, enter note");

    private final String textMessage;

    ErrorMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public String getTextMessage() {
        return textMessage;
    }
}