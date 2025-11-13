package com.example;

public class messageToJson {
    public String topic;
    public String message;
    public messageToJson(String topic, String message){
        this.topic = topic;
        this.message = message;
    }
    public  String getTopic(){
        return topic;
    }
    public String getMessage(){
        return message;
    }
    public void setTopic(String topic){
        this.topic = topic;
    }
    public void setMessage(String message){
        this.message = message;
    }
}
