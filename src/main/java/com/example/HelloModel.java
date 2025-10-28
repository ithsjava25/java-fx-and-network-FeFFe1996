package com.example;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Model layer: encapsulates application data and business logic.
 */
public class HelloModel {
    private StringProperty msgBoard;

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
