package com.example;

import io.github.cdimascio.dotenv.Dotenv;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Model layer: encapsulates application data and business logic.
 */
public class HelloModel {
    ObservableList<String> msgList = FXCollections.observableArrayList();
    private final String hostName;
    TextFlow text_flow = new TextFlow();
    Text timeStamp = new Text(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm")));

    HelloModel(){
        Dotenv dotenv = Dotenv.load();
        hostName = Objects.requireNonNull(dotenv.get("HOST_NAME"));
        msgList.add("Hello World!");
    }

    public ObservableList<String> getMsgList() {
        return msgList;
    }

    public void setMsgList(ObservableList<String> msgList) {
        this.msgList = msgList;
    }

    public void sendMsg( TextField userName, TextField msg) {
        //Todo: send message with httpclient
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .timeout(Duration.ofSeconds(20))
                .POST(HttpRequest.BodyPublishers.ofString("Hello World"))
                .uri(URI.create(hostName + "/myTopic"))
                .build();
        try {
            //TODO: handle long blocking send request so application doesnt freeze
            //1. use thread send message
            //2. use async
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            msgList.add(response.toString());
        }catch (IOException e){
            System.out.println("IOException sending message");
        }catch (InterruptedException e){
            System.out.println("Interrupted sending message");
        }
    }
}
