package sk.upjs.paz.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sk.upjs.paz.app.SceneManager;
import sk.upjs.paz.model.Message;
import sk.upjs.paz.model.User;
import javafx.scene.control.Label;

import javafx.geometry.Insets;
import sk.upjs.paz.service.MessageService;

import java.time.LocalDate;
import java.awt.*;
import java.awt.event.ActionEvent;


public class MessageController {

    private static User currentUser = SceneManager.getCurrentUser();
    private final MessageService messageService = new MessageService();




    @FXML
    private VBox messagesContainer;

    @FXML
    private TextArea messageInput;

    @FXML
    private Button sendButton;

    @FXML
    public void initialize() {
        applyRolePermissions();
        loadMessagesFromDb();
    }

    private void loadMessagesFromDb() {

        messagesContainer.getChildren().clear();

        for (Message msg : messageService.getAllMessages()) {
            messagesContainer.getChildren().add(
                    createMessageCard(
                            msg.getDateSent(),
                            msg.getText()
                    )
            );
        }
    }


    private void applyRolePermissions() {
        int role = currentUser.getRoleId();

        if (role == 3) {
            messageInput.setEditable(false);
            messageInput.setDisable(true);
            sendButton.setDisable(true);
        }
    }




    @FXML
    private void close(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();
        stage.close();
    }

    @FXML
    private void sendMessage() {

        String text = messageInput.getText();

        if (text == null || text.isBlank()) {
            return;
        }

        messageService.saveMessage(currentUser, text);

        VBox messageCard = createMessageCard(
                LocalDate.now().toString(),
                text
        );

        messagesContainer.getChildren().add(0, messageCard);

        messageInput.clear();
    }


    private VBox createMessageCard(String date, String text) {

        Label dateLabel = new Label(date);
        dateLabel.getStyleClass().add("passport-sub");

        Label messageLabel = new Label(text);
        messageLabel.setWrapText(true);
        messageLabel.getStyleClass().add("passport-name");

        VBox box = new VBox(8, dateLabel, messageLabel);
        box.getStyleClass().add("data-row");
        box.setPadding(new Insets(10));

        return box;
    }

}
