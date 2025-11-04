package com.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.scene.layout.VBox;

import java.util.stream.Collectors;


/**
 * Controller layer: mediates between the view (FXML) and the model.
 */
public class HelloController {
    private final HelloModel model = new HelloModel(null);

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
    }

    public HelloModel getModel() {
        return model;
    }

    public void submitMsg(ActionEvent actionEvent) {
        if (msg.getText().isEmpty()) {
            errorIsEmpty.setVisible(true);
            errorIsEmpty.setText("Please enter a message");
        }else {
            model.setMsgToSend(msg.getText());
            model.sendMsg();
            msg.clear();
            errorIsEmpty.setVisible(false);
        }

    }
}
