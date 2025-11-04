package com.example;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.stream.Collectors;

/**
 * Model layer: encapsulates application data and business logic.
 */
public class HelloModel{
    private final NtfyConnection connection;

    private final StringProperty msgToSend = new SimpleStringProperty();

    private final ObservableList<NtfyMessageDto> msgList = FXCollections.observableArrayList();
    private final ObservableList<String> message = FXCollections.observableArrayList();


    public HelloModel(NtfyConnection connection){
        this.connection = connection;
        receiveMsg();
    }

    public ObservableList<String> getMessage() {
            //message = message.add(msgList.stream().map(p -> p.message()).collect(Collectors.toCollection()));
            return message;
    }

    public ObservableList<NtfyMessageDto> getMsgList() {
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
