package com.example;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Model layer: encapsulates application data and business logic.
 */
public class HelloModel{
    private final NtfyConnection connection;

    private final StringProperty msgToSend = new SimpleStringProperty();

    private final ObservableList<NtfyMessageDto> msgList = FXCollections.observableArrayList();
    private final ObservableList<String> message = FXCollections.observableArrayList();


    public HelloModel(NtfyConnection connection){
        receiveMsg();
        this.connection = connection;
    }

    public ObservableList<String> getMessage() {
            return message;
    }

    public ObservableList<NtfyMessageDto> getMsgList() {
        msgList.stream().map(NtfyMessageDto::message).forEach(message::add);
        return msgList;
    }

    public String getMsgToSend() {
        return msgToSend.get();
    }

    public StringProperty msgToSendProperty() {
        return msgToSend;
    }

    public void setMsgToSend(String msgToSend) {
        this.msgToSend.set(msgToSend);
    }

    public void sendMsg() {
        connection.send(msgToSend.get());
    }

    public void receiveMsg(){
        connection.receive(m -> Platform.runLater(
                ()-> msgList.add(m)));
    }
}
