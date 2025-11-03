package com.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;


/**
 * Controller layer: mediates between the view (FXML) and the model.
 */
public class HelloController {
    private final HelloModel model = new HelloModel();

    public ListView<NtfyMessageDto> messageBoard;

    public VBox newMessage;
    public TextField msg;
    public Button submitButton;

    @FXML
    private Label messageLabel;

    @FXML
    private void initialize() {
        messageBoard.setItems(model.getMsgList());
        model.receiveMsg();
    }

    public HelloModel getModel() {
        return model;
    }

    public void submitMsg(ActionEvent actionEvent) {
        model.sendMsg(msg.getText());
        msg.clear();
    }
}
