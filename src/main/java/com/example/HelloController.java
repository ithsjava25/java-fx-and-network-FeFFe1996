package com.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

/**
 * Controller layer: mediates between the view (FXML) and the model.
 */
public class HelloController {
    private final HelloModel model = new HelloModel(new NtfyConnectionImpl());

    public ListView<NtfyMessageDto> messageBoard;
    public ListView<String> messageList;
    public VBox newMessage;
    public TextField msg;
    public Button submitButton;
    public Label errorIsEmpty;
    public TextField topic;
    public Label errorTopicIsEmpty;

    @FXML
    private Label messageLabel;

    @FXML
    private void initialize() {
        //messageBoard.setItems(model.getMsgList());
        messageList.setItems(model.getMessage());
        errorIsEmpty.setVisible(false);
        errorTopicIsEmpty.setVisible(false);
    }

    public HelloModel getModel() {
        return model;
    }

    public void submitMsg(ActionEvent actionEvent) {
        if (topic.getText().isEmpty()) {
            errorTopicIsEmpty.setVisible(true);
            errorTopicIsEmpty.setText("Please enter a topic");
        }else if (msg.getText().isEmpty()) {
            errorIsEmpty.setVisible(true);
            errorIsEmpty.setText("Please enter a message");
        }else {
            model.setMsgToSend(msg.getText());
            model.setMsgTopic(topic.getText());
            model.sendMsg();
            msg.clear();
            errorIsEmpty.setVisible(false);
            errorTopicIsEmpty.setVisible(false);
        }

    }
}
