package com.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;

import java.util.stream.Collectors;


/**
 * Controller layer: mediates between the view (FXML) and the model.
 */
public class HelloController {
    private final HelloModel model = new HelloModel();

    public ListView<NtfyMessageDto> messageBoard;
    public ListView<String> messageList;
    public VBox newMessage;
    public TextField msg;
    public Button submitButton;
    public Label errorIsEmpty;

    @FXML
    private Label messageLabel;

    @FXML
    private void initialize() {
        messageBoard.setItems(model.getMsgList());
        errorIsEmpty.setVisible(false);
        model.receiveMsg();
    }

    public HelloModel getModel() {
        return model;
    }

    public void submitMsg(ActionEvent actionEvent) {
        if (msg.getText().equals("")) {
            errorIsEmpty.setVisible(true);
            errorIsEmpty.setText("Please enter a message");
        }else {
            model.sendMsg(msg.getText());
            msg.clear();
            errorIsEmpty.setVisible(false);
        }

    }
}
