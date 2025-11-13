package com.example;

import com.fasterxml.jackson.core.JsonProcessingException;

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
    private boolean testChecker = false;

    public NtfyConnectionImpl(){
        Dotenv dotenv = Dotenv.load();
        hostName = Objects.requireNonNull(dotenv.get("HOST_NAME"));
    }

    public NtfyConnectionImpl(String hostName){
        this.hostName = hostName;
    }

    public void setTestChecker(boolean testChecker){
        this.testChecker = testChecker;
    }

    public boolean checkTest(){
        return testChecker;
    }

    @Override
    public boolean send(String topic, String message) {
                try {
                    HttpRequest request = getHttpRequest(topic, message);
                    var response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    return true;
                } catch (IOException e) {
                    System.out.println("IOException sending message");
                    return false;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Interrupted sending message");
                    return false;
            }
    }

    private HttpRequest getHttpRequest(String topic, String message) {
        messageToJson newMessage = new messageToJson(topic, message);
        final String Json;
        try {
            Json = mapper.writeValueAsString(newMessage);
        } catch (IllegalStateException e){
            throw new IllegalStateException("Failure to serialize: ", e);
        }

        String url = hostName+"/"+ topic +"/json"; //used for testing fake server
        if(checkTest()){
            return HttpRequest.newBuilder()
                    .timeout(Duration.ofSeconds(10))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(Json))
                    //.POST(HttpRequest.BodyPublishers.ofString("Hello World")) //for testing purposes
                    .uri(URI.create(url))
                    .build();
        } else {
        return HttpRequest.newBuilder()
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(Json))
                //.POST(HttpRequest.BodyPublishers.ofString("Hello World")) //for testing purposes
                .uri(URI.create(hostName))
                .build();
    }
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
                        .map(s -> {
                            try {
                                return mapper.readValue(s, NtfyMessageDto.class);
                            }catch (IllegalStateException e){
                                throw new IllegalStateException("Failure to serialize: ",e);
                            }
                        })
                        .filter(message->message.event().equals("message"))
                        //.peek(System.out::println) //debugger to check messages that comes in
                        .forEach(messageHandler)).exceptionally(error -> {
                    System.out.println("Error message: " + error.getMessage());
                    return null;
                });

        receiveMsg.thenAccept(System.out::println);
    }
}
