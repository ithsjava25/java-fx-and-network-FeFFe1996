package com.example;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;

/**
 * Controller layer: mediates between the view (FXML) and the model.
 */
public class HelloController {
    public ListView<String> listView;
    private final HelloModel model = new HelloModel();
    public TextFlow messageBoard;
    public VBox newMessage;
    public TextField userName;
    public TextField msg;
    public Button submitButton;

    @FXML
    private Label messageLabel;

    @FXML
    private void initialize() {
        listView = new ListView<>();
        messageBoard.getChildren().add(model.text_1);
    }

    private void getMessage() {

    }

    public HelloModel getModel() {
        return model;
    }

}
