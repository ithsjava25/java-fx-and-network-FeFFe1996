package com.example;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;

/**
 * Controller layer: mediates between the view (FXML) and the model.
 */
public class HelloController {
    public ObservableList<String> listView = FXCollections.observableArrayList();
    private final HelloModel model = new HelloModel();

    public TextFlow messageBoard;

    public VBox newMessage;
    public TextField msg;
    public Button submitButton;

    @FXML
    private Label messageLabel;

    @FXML
    private void initialize() {
        messageBoard.getChildren().add(model.msgList);
        listView.add(model.msgList.getSelectionModel().getSelectedItem());
        model.receiveMsg();
    }

    public HelloModel getModel() {
        return model;
    }

    public void submitMsg(ActionEvent actionEvent) {
        model.sendMsg();
        msg.clear();
    }
}
