package com.example;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Model layer: encapsulates application data and business logic.
 */
public class HelloModel {
    private final StringProperty messageBoardProperty;
    public HelloModel() {
        messageBoardProperty = new SimpleStringProperty("");
    }

    public void setMessageBoardProperty(String messageBoardProperty) {
        messageBoardProperty.isEmpty();
    }

    public String getMessageBoard() {
        return messageBoardProperty.get();
    }

    public String getGreeting() {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        return "Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".";
    }

    public String getMessage() {
        return "";
    }
}
