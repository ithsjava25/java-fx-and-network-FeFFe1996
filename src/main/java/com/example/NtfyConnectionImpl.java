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
        messageToJson newMessage = new messageToJson("mytopic", message);
        String Json = mapper.writeValueAsString(newMessage);

        HttpRequest request = HttpRequest.newBuilder()
                .timeout(Duration.ofSeconds(20))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(Json))
                //.POST(HttpRequest.BodyPublishers.ofString("Hello World")) //for testing purposes
                .uri(URI.create(hostName))
                .build();
            try {
                //TODO: handle long blocking send request so application doesnt freeze
                //1. use thread send message
                //2. use async
                var response = client.send(request, HttpResponse.BodyHandlers.ofString());
                return true;
            } catch (IOException e) {
                System.out.println("IOException sending message");
            } catch (InterruptedException e) {
                System.out.println("Interrupted sending message");
            }
            return false;
    }

    @Override
    public void receive(Consumer<NtfyMessageDto> messageHandler) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(hostName + "/mytopic/json"))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofLines())
                .thenAccept(response -> response.body()
                        .map(s -> mapper.readValue(s, NtfyMessageDto.class))
                        .filter(message->message.event().equals("message"))
                        //.peek(System.out::println) //debugger to check messages that comes in
                        .forEach(messageHandler));
    }
}
