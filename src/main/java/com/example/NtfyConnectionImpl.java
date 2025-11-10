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
import java.util.concurrent.CompletableFuture;
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
            Thread.ofPlatform().start(() -> {
                try {
                    HttpRequest request = getHttpRequest(topic, message);
                    var response = client.send(request, HttpResponse.BodyHandlers.ofString());
                } catch (IOException e) {
                    System.out.println("IOException sending message");
                } catch (InterruptedException e) {
                    System.out.println("Interrupted sending message");
            }});
            return false;
    }

    private HttpRequest getHttpRequest(String topic, String message) {
        messageToJson newMessage = new messageToJson(topic, message);
        String Json = mapper.writeValueAsString(newMessage);
        String url = hostName+"/"+ topic +"/json"; //used for testing fake server
        HttpRequest request = HttpRequest.newBuilder()
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(Json))
                //.POST(HttpRequest.BodyPublishers.ofString("Hello World")) //for testing purposes
                .uri(URI.create(hostName))
                .build();
        return request;
    }

    @Override
    public void receive(String topic, Consumer<NtfyMessageDto> messageHandler) {
        String url = hostName+"/"+topic+"/json";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        CompletableFuture<Void> receiveMsg = client.sendAsync(request, HttpResponse.BodyHandlers.ofLines())
                .thenAccept(response -> response.body()
                        .map(s -> mapper.readValue(s, NtfyMessageDto.class))
                        .filter(message->message.event().equals("message"))
                        //.peek(System.out::println) //debugger to check messages that comes in
                        .forEach(messageHandler)).exceptionally(error -> {
                    System.out.println("Error message: " + error.getMessage());
                    return null;
                });
        receiveMsg.thenAccept(System.out::println);
    }
}
