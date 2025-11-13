package com.example;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Model layer: encapsulates application data and business logic.
 */
public class HelloModel{
    private final NtfyConnection connection;

    private final StringProperty msgToSend = new SimpleStringProperty();
    private final StringProperty msgTopic = new SimpleStringProperty();

    private final ObservableList<NtfyMessageDto> msgList = FXCollections.observableArrayList();
    private final ObservableList<String> message = FXCollections.observableArrayList();

    public HelloModel(NtfyConnection connection){
        this.connection = connection;
    }

    private static void runOnFx(Runnable task){
        try{
            if (Platform.isFxApplicationThread()){
                task.run();
            } else {
                Platform.runLater(task);
            }
        }catch(IllegalStateException notInitialized){
            task.run();
        }
    }
    public String getMsgTopic() {
        return msgTopic.get();
    }

    public StringProperty msgTopicProperty() {
        return msgTopic;
    }

    public void setMsgTopic(String msgTopic) {
        runOnFx(() -> {
            var normalizeString = msgTopic.trim().replaceAll(" ", "");
            this.msgTopic.set(normalizeString);
            if (!normalizeString.isEmpty()){
                receiveMsg();
            }
        });
    }

    public ObservableList<String> getMessage() {
            return message;
    }

    public void clearMessage() {
            message.clear();
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
        runOnFx(() -> this.msgToSend.set(msgToSend.trim()));
    }

    public boolean checkTopicIsNotEmpty(String topic){
        if(topic.trim().isEmpty()){
            System.out.println("Error: topic cannot be empty");
            return true;
        }
        System.out.println("Valid topic");
        return false;
    }

    public boolean checkMessageIsNotEmpty(String message){
        if(message.isEmpty()){
            System.out.println("Error: message cannot be empty");
            return true;
        } else if (message.isBlank()) {
            System.out.println("Error: message cannot be blank");
            return true;
        }else {
            return false;
        }
    }

    public void sendMsg() {
        connection.send(getMsgTopic(), getMsgToSend());
    }


    public void receiveMsg(){
        var currentTopic = getMsgTopic();
        if(currentTopic == null || currentTopic.isBlank()){
            return;
        }
            connection.receive(getMsgTopic(), m -> {
                runOnFx(
                        ()-> msgList.add(m));
                runOnFx(
                        () -> message.setAll(msgList.stream().filter(p -> p.topic().equals(getMsgTopic())).map(NtfyMessageDto::message).collect(Collectors.toList()))
                );
            });
    }

}
