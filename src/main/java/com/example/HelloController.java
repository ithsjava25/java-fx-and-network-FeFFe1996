package com.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.text.TextFlow;

/**
 * Controller layer: mediates between the view (FXML) and the model.
 */
public class HelloController {
    public ListView<String> listView;
    private final HelloModel model = new HelloModel();
    public TextFlow messageBoard;

    @FXML
    private Label messageLabel;

    @FXML
    private void initialize() {
        listView = new ListView<>();

    }
}
