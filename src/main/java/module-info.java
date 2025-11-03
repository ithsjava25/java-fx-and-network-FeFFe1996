module hellofx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires io.github.cdimascio.dotenv.java;

    opens com.example to javafx.fxml;
    exports com.example;
}