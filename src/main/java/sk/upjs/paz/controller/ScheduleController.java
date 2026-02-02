package sk.upjs.paz.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import sk.upjs.paz.app.I18n;
import sk.upjs.paz.app.SceneManager;
import sk.upjs.paz.model.Term;
import sk.upjs.paz.model.User;
import sk.upjs.paz.service.ScheduleService;

import java.util.List;

public class ScheduleController {

    @FXML private CheckBox myRegistrationsCheckBox;
    @FXML private VBox termsContainer;
    @FXML private CheckBox activeRegistrationsCheckBox;

    private final ScheduleService scheduleService = new ScheduleService();

    private List<Term> allTerms;
    private boolean canManageTerms;
    private User currentUser;

    @FXML
    private void initialize() {
        currentUser = SceneManager.getCurrentUser();

        canManageTerms =
                scheduleService.hasRole(currentUser.getIdUser(), "ADMIN")
                        || scheduleService.hasRole(currentUser.getIdUser(), "OFFICER");

        myRegistrationsCheckBox.setOnAction(e -> applyFilters());
        activeRegistrationsCheckBox.setOnAction(e -> applyFilters());

        applyFilters();
    }

    private void applyFilters() {
        termsContainer.getChildren().clear();

        List<Term> terms = scheduleService.filterTerms(
                currentUser,
                myRegistrationsCheckBox.isSelected(),
                activeRegistrationsCheckBox.isSelected()
        );

        for (Term term : terms) {
            termsContainer.getChildren().add(createTermCard(term));
        }
    }

    @FXML
    private void backToProfile() {
        SceneManager.openUserScene(currentUser);
    }

    @FXML
    private void onCreateTerm() {
        // якщо ти вже пофіксив SceneManager - тут має бути твій правильний метод
        SceneManager.openCreateEditTermWindow(null);
    }

    private VBox createTermCard(Term term) {

        Label title = new Label(term.getType());
        title.getStyleClass().add("term-title");

        Label date = new Label(
                term.getDate() + " " +
                        term.getStartTime() + " – " +
                        term.getEndTime()
        );
        date.getStyleClass().add("term-info");

        Label address = new Label(term.getAddress());
        address.getStyleClass().add("term-info");

        Label capacity = new Label(
                I18n.bundle().getString("schedule.registered") + ": " +
                        scheduleService.getRegisteredCount(term.getIdTerms()) +
                        " / " + term.getCapacity()
        );
        capacity.getStyleClass().add("term-info");

        Button registerButton = new Button();
        registerButton.getStyleClass().add("primary-btn");
        registerButton.setUserData(term);
        registerButton.setOnAction(this::toggleRegistration);

        long userId = SceneManager.getCurrentUser().getIdUser();
        boolean registered =
                scheduleService.isUserRegistered(userId, term.getIdTerms());

        registerButton.setText(
                registered
                        ? I18n.bundle().getString("schedule.unregister")
                        : I18n.bundle().getString("schedule.register")
        );

        VBox info = new VBox(5, date, address, capacity);

        HBox actions = new HBox(10, registerButton);

        if (canManageTerms) {

            Button editButton = new Button("✏");
            editButton.getStyleClass().add("secondary-btn");
            editButton.setOnAction(e -> SceneManager.openCreateEditTermWindow(term));

            Button deleteButton = new Button("🗑");
            deleteButton.getStyleClass().add("secondary-btn");
            deleteButton.setOnAction(e -> deleteTerm(term));

            actions.getChildren().addAll(editButton, deleteButton);
        }

        HBox content = new HBox(20, info, actions);

        VBox card = new VBox(10, title, content);
        card.getStyleClass().add("term-card");
        card.setPadding(new Insets(16));

        return card;
    }

    @FXML
    private void toggleRegistration(ActionEvent event) {

        Button button = (Button) event.getSource();
        Term term = (Term) button.getUserData();

        long userId = SceneManager.getCurrentUser().getIdUser();
        long termId = term.getIdTerms();

        String registerText = I18n.bundle().getString("schedule.register");
        String unregisterText = I18n.bundle().getString("schedule.unregister");

        if (registerText.equals(button.getText())) {
            scheduleService.register(userId, termId);
            button.setText(unregisterText);
        } else {
            scheduleService.unregister(userId, termId);
            button.setText(registerText);
        }

        allTerms = scheduleService.getVisibleTermsForUser(SceneManager.getCurrentUser());
        applyFilters();
    }

    private void deleteTerm(Term term) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(I18n.bundle().getString("common.confirmation"));
        alert.setHeaderText(I18n.bundle().getString("schedule.delete.header"));
        alert.setContentText(term.getType() + " – " + term.getDate());

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            scheduleService.cancelTerm(term.getIdTerms());
            applyFilters();
        }
    }

    public boolean isTermFull(Term term) {
        return scheduleService.getRegisteredCount(term.getIdTerms()) >= term.getCapacity();
    }
}
