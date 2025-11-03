package com.example;

import com.google.gson.Gson;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

import java.io.IOException;
import java.lang.reflect.Type;
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
    ListView<String> msgList = new ListView<>();
    private final HttpClient client = HttpClient.newHttpClient();
    private final String hostName;
    public Gson gson = new Gson();
    private String topicName;
    Text timeStamp = new Text(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm")));

    HelloModel(){
        Dotenv dotenv = Dotenv.load();
        hostName = Objects.requireNonNull(dotenv.get("HOST_NAME"));
    }

    public String createJson(){
        return gson.toJson("hello world");
    }

    public void sendMsg() {
        //Todo: send message with httpclient
        HttpRequest request = HttpRequest.newBuilder()
                .timeout(Duration.ofSeconds(20))
                .POST(HttpRequest.BodyPublishers.ofString(createJson()))
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
                .thenAccept(response -> response.body().forEach(System.out::println));
    }
}
