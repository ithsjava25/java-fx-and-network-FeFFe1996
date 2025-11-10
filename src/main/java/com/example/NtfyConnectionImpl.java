package com.example;

import io.github.cdimascio.dotenv.Dotenv;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Objects;
import java.util.function.Consumer;

public class NtfyConnectionImpl implements NtfyConnection {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String hostName;
    private final ObjectMapper mapper = new ObjectMapper();

    public NtfyConnectionImpl(){
        Dotenv dotenv = Dotenv.load();
        hostName = Objects.requireNonNull(dotenv.get("HOST_NAME"));
    }

    public NtfyConnectionImpl(String hostName){
        this.hostName = hostName;
    }

    @Override
    public boolean send(String topic, String message) {
        //TODO: handle long blocking send request so application doesnt freeze
        //1. use thread send message
        //2. use async
            Thread.ofPlatform().start(() -> {
                try {
                    messageToJson newMessage = new messageToJson(topic, message);
                    String Json = mapper.writeValueAsString(newMessage);
                    String url = hostName+"/"+topic+"/json"; //used for testing fake server
                    HttpRequest request = HttpRequest.newBuilder()
                            .timeout(Duration.ofSeconds(20))
                            .header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(Json))
                            //.POST(HttpRequest.BodyPublishers.ofString("Hello World")) //for testing purposes
                            .uri(URI.create(hostName))
                            .build();
                    var response = client.send(request, HttpResponse.BodyHandlers.ofString());
                } catch (IOException e) {
                    System.out.println("IOException sending message");
                } catch (InterruptedException e) {
                    System.out.println("Interrupted sending message");
            }});
            return false;
    }

    @Override
    public void receive(String topic, Consumer<NtfyMessageDto> messageHandler) {
        String url = hostName+"/"+topic+"/json";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofLines())
                .thenAccept(response -> response.body()
                        .map(s -> mapper.readValue(s, NtfyMessageDto.class))
                        .filter(message->message.event().equals("message"))
                        //.peek(System.out::println) //debugger to check messages that comes in
                        .forEach(messageHandler));
    }
}
