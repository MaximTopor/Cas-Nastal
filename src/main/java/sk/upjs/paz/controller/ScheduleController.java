package sk.upjs.paz.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import sk.upjs.paz.app.SceneManager;
import sk.upjs.paz.model.Term;
import sk.upjs.paz.service.ScheduleService;
import sk.upjs.paz.service.UserService;

import java.time.LocalDate;
import java.util.List;

public class ScheduleController {

    @FXML
    private VBox termsContainer;

    @FXML
    private CheckBox myRegistrationsCheckBox;

    @FXML
    private CheckBox activeRegistrationsCheckBox;

    @FXML
    private Button createTermButton;

    private final ScheduleService scheduleService = new ScheduleService();
    private final UserService userService = new UserService();

    private List<Term> allTerms;
    private boolean canManageTerms;

    @FXML
    private void initialize() {
        System.out.println(">>> ScheduleController.initialize()");

        long userId = SceneManager.getCurrentUser().getIdUser();

        // ✅ ROLE CHECK THROUGH SERVICE
        canManageTerms = userService.canManageTerms(userId);

        // Create term button (role-based)
        createTermButton.setVisible(canManageTerms);
        createTermButton.setManaged(canManageTerms);

        // Load data ONCE
        allTerms = scheduleService.getVisibleTermsForUser(
                SceneManager.getCurrentUser()
        );

        // Filters
        myRegistrationsCheckBox.selectedProperty()
                .addListener((obs, o, n) -> applyFilters());

        activeRegistrationsCheckBox.selectedProperty()
                .addListener((obs, o, n) -> applyFilters());

        applyFilters();
    }

    private void applyFilters() {
        termsContainer.getChildren().clear();

        for (Term term : allTerms) {
            termsContainer.getChildren().add(createTermCard(term));
        }
    }

    @FXML
    private void backToProfile() {
        SceneManager.backToProfile();
    }

    @FXML
    private void onCreateTerm() {
        SceneManager.openCreateTerm();
    }

    private VBox createTermCard(Term term) {

        Label title = new Label(term.getType());
        title.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        Label date = new Label(
                term.getDate() + " " +
                        term.getStartTime() + " – " +
                        term.getEndTime()
        );

        Label address = new Label(term.getAddress());
        Label capacity = new Label(
                "Prihlásení: " + scheduleService.getRegisteredCount(term.getIdTerms()) + " / " + term.getCapacity()
        );



        Button registerButton = new Button();
        if (isTermFull(term)) {
            registerButton.setDisable(true);
        } else if (!isTermFull(term) && registerButton.isDisable()) {
            registerButton.setDisable(false);
        }
        registerButton.setUserData(term);
        registerButton.setOnAction(this::toggleRegistration);

        long userId = SceneManager.getCurrentUser().getIdUser();
        boolean registered =
                scheduleService.isUserRegistered(userId, term.getIdTerms());

        Button registerButton = new Button(
                registered ? "Odhlásiť sa" : "Zapísať sa"
        );
        registerButton.setUserData(term);
        registerButton.setOnAction(this::toggleRegistration);

        VBox info = new VBox(5, date, address, capacity);
        HBox content = new HBox(20, info, registerButton);

        // 🗑 DELETE BUTTON (ADMIN / WORKER only)
        if (canManageTerms) {
            Button deleteButton = new Button("🗑");
            deleteButton.setOnAction(e -> deleteTerm(term));

            actions.getChildren().addAll(editButton, deleteButton);
        }

        HBox content = new HBox(20, info, actions);

        VBox card = new VBox(10, title, content);
        card.setStyle("-fx-border-color: black; -fx-border-width: 2;");
        card.setPadding(new javafx.geometry.Insets(10));

        return card;
    }


    @FXML
    private void toggleRegistration(ActionEvent event) {

        Button button = (Button) event.getSource();
        Term term = (Term) button.getUserData();

        long userId = SceneManager.getCurrentUser().getIdUser();
        long termId = term.getIdTerms();

        if ("Zapísať sa".equals(button.getText())) {
            scheduleService.register(userId, termId);
            button.setText("Odhlásiť sa");
        } else {
            scheduleService.unregister(userId, termId);
            button.setText("Zapísať sa");
        }

        allTerms = scheduleService.getVisibleTermsForUser(
                SceneManager.getCurrentUser()
        );
        applyFilters();
    }

    private void deleteTerm(Term term) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Potvrdenie");
        alert.setHeaderText("Vymazať termín?");
        alert.setContentText(
                term.getType() + " – " + term.getDate()
        );

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            scheduleService.cancelTerm(term.getIdTerms());
            allTerms = scheduleService.getAllTerms();
            applyFilters();
        }
    }

    public boolean isTermFull(Term term) {
        return scheduleService.getRegisteredCount(term.getIdTerms()) >= term.getCapacity();
    }

}
