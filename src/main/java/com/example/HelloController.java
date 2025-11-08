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
    public Label yourTopic;
    public Label yourMsg;
    public Button changeTopicButton;

    @FXML
    private Label messageLabel;

    @FXML
    private void initialize() {
        //messageBoard.setItems(model.getMsgList());
//        topic.textProperty().addListener((observable, oldValue, newValue) -> {
//            System.out.println(newValue);
//        }); used to se input in topic window
        model.setMsgTopic(topic.getText());
        messageList.setItems(model.getMessage());
        errorIsEmpty.setVisible(false);
        errorTopicIsEmpty.setVisible(false);
        submitButton.setDisable(true);
    }

    public HelloModel getModel() {
        return model;
    }

    public void submitMsg(ActionEvent actionEvent) {
        model.setMsgToSend(msg.getText());
        if (model.checkMessageIsNotEmpty(model.getMsgToSend())) {
            errorIsEmpty.setVisible(true);
            errorIsEmpty.setText("Please enter a message");
        }else {

            model.sendMsg();
            msg.clear();
            errorIsEmpty.setVisible(false);
        }
    }

    public void changeTopic(ActionEvent actionEvent) {
        model.setMsgTopic(topic.getText());
        if (model.checkTopicIsNotEmpty(model.getMsgTopic())) {
            errorTopicIsEmpty.setVisible(true);
            errorTopicIsEmpty.setText("Please enter a topic");
        }else {

        errorTopicIsEmpty.setVisible(false);
        submitButton.setDisable(false);
        model.receiveMsg();
        model.clearMessage();
    }}
}
