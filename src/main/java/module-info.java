module hellofx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires io.github.cdimascio.dotenv.java;
    requires java.net.http;
    requires com.google.gson;
    requires tools.jackson.databind;
    requires javafx.base;
    requires javafx.graphics;
    requires com.fasterxml.jackson.core;

    opens com.example to javafx.fxml;
    exports com.example;
}