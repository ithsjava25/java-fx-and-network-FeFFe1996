package com.example;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Model layer: encapsulates application data and business logic.
 */
public class HelloModel {
    private StringProperty msgBoard;
    TextFlow text_flow = new TextFlow();
    Text text_1 = new Text("Hello World");

    Text timeStamp = new Text(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm")));

    public TextFlow getText_flow() {
        return text_flow;
    }

    public void setText_flow(TextFlow text_flow) {
        this.text_flow = text_flow;
    }

    public Text getText_1() {
        return text_1;
    }

    public Text getTimeStamp() {
        return timeStamp;
    }

    public String getMsgBoard() {
        return msgBoard.get();
    }

    public StringProperty msgBoardProperty() {
        return msgBoard;
    }

    public void setMsgBoard(String msgBoard) {
        this.msgBoard.set(msgBoard);
    }
}
