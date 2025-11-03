package com.example;

import io.github.cdimascio.dotenv.Dotenv;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tools.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Objects;


/**
 * Model layer: encapsulates application data and business logic.
 */
public class HelloModel {
    //private final ArrayList<NtfyMessageDto> msgList = new ArrayList<>();
    private final ObjectMapper mapper = new ObjectMapper();

    private final ObservableList<NtfyMessageDto> msgList = FXCollections.observableArrayList();
    private final HttpClient client = HttpClient.newHttpClient();
    private final String hostName;
    private String topicName;


    HelloModel(){
        Dotenv dotenv = Dotenv.load();
        hostName = Objects.requireNonNull(dotenv.get("HOST_NAME"));
    }

    public ObservableList<NtfyMessageDto> getMsgList() {
        return msgList;
    }

    public void sendMsg(String msg) {

        HttpRequest request = HttpRequest.newBuilder()
                .timeout(Duration.ofSeconds(20))
                .POST(HttpRequest.BodyPublishers.ofString("Hello World!"))
                .uri(URI.create(hostName + "/mytopic"))
                .build();
        try {
            //TODO: handle long blocking send request so application doesnt freeze
            //1. use thread send message
            //2. use async
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }catch (IOException e){
            System.out.println("IOException sending message");
        }catch (InterruptedException e){
            System.out.println("Interrupted sending message");
        }
    }

    public void receiveMsg(){
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(hostName + "/mytopic/json"))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofLines())
                .thenAccept(response -> response.body()
                        .map(s -> mapper.readValue(s, NtfyMessageDto.class))
                        .filter(message->message.event().equals("message"))
                        //.peek(System.out::println) //debugger to check messages that comes in
                        .forEach(s ->
                                Platform.runLater(() -> msgList.add(s))));
    }
}
