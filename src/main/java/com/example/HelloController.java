package com.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controller layer: mediates between the view (FXML) and the model.
 */
public class HelloController {
    public ObservableList<Label> chatList = FXCollections.observableArrayList();
    private final HelloModel model = new HelloModel();
    public Label messageBoard;

    @FXML
    private Label messageLabel;

    @FXML
    private void initialize() {
        if (messageLabel != null) {
            messageLabel.setText(model.getGreeting());
        }
        if (messageBoard != null) {
            messageBoard.setText(chatList.toString());
        }
    }
}
