package sk.upjs.paz.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import sk.upjs.paz.model.Status;
import sk.upjs.paz.model.User;
import sk.upjs.paz.service.StatusHistoryService;

public class StatusManagementController {

    @FXML
    private Label userLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private TextArea reasonField;

    private User user;
    private Status oldStatus;
    private Status newStatus;
    private long changedBy;

    private final StatusHistoryService statusHistoryService = new StatusHistoryService();

    public void init(User user, Status oldStatus, Status newStatus, long changedBy) {
        this.user = user;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.changedBy = changedBy;

        userLabel.setText("Користувач: " + user.getName() + " " + user.getSurname());
        statusLabel.setText("Статус: " + oldStatus.getName() + " → " + newStatus.getName());
    }

    @FXML
    private void confirm() {
        String reason = (reasonField != null) ? reasonField.getText() : null;
        if (reason == null || reason.isBlank()) {
            return;
        }

        statusHistoryService.changeStatus(
                user.getIdUser(),
                newStatus.getIdStatus(),
                changedBy,
                reason.trim()
        );

        closeThisWindow();
    }

    @FXML
    private void cancel() {
        closeThisWindow();
    }

    private void closeThisWindow() {
        if (reasonField == null || reasonField.getScene() == null) {
            return;
        }
        Stage stage = (Stage) reasonField.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }
    }
}
