package sk.upjs.paz.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import sk.upjs.paz.app.SceneManager;
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

    private final StatusHistoryService statusHistoryService =
            new StatusHistoryService();

    /**
     * Ініціалізація з SceneManager
     */
    public void init(
            User user,
            Status oldStatus,
            Status newStatus,
            long changedBy
    ) {
        this.user = user;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.changedBy = changedBy;

        userLabel.setText(
                "Користувач: " + user.getName() + " " + user.getSurname()
        );

        statusLabel.setText(
                "Статус: " + oldStatus.getName() + " → " + newStatus.getName()
        );
    }

    @FXML
    private void confirm() {

        String reason = reasonField.getText();

        if (reason == null || reason.isBlank()) {
            // Мінімальна валідація — без Alert, як у твоєму стилі
            return;
        }

        statusHistoryService.changeStatus(
                user.getIdUser(),
                newStatus.getIdStatus(),
                changedBy,
                reason
        );

        SceneManager.closeModal();
    }

    @FXML
    private void cancel() {
        SceneManager.closeModal();
    }
}
