package sk.upjs.paz.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import sk.upjs.paz.app.SceneManager;
import sk.upjs.paz.model.Term;
import sk.upjs.paz.model.User;
import sk.upjs.paz.service.ScheduleService;

public class ScheduleController {

    @FXML
    private VBox termsContainer;

    User currentUser = SceneManager.getCurrentUser();

    private final ScheduleService scheduleService = new ScheduleService();

    @FXML
    private void initialize() {
        System.out.println(">>> ScheduleController.initialize()");
        loadTerms();
    }

    private void loadTerms() {
        System.out.println(">>> loadTerms() called");
        termsContainer.getChildren().clear();

        var terms = scheduleService.getAllTerms();

        for (Term term : scheduleService.getAllTerms()) {
            System.out.println(">>> TERM: " + term);
            termsContainer.getChildren().add(createTermCard(term));
        }
        System.out.println(">>> terms size = " + terms.size());
    }

    @FXML
    private void backToProfile() {
        SceneManager.backToProfile();
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
        Label capacity = new Label("Kapacita: " + term.getCapacity());

        Button button = new Button("Zapísať sa");
        button.setUserData(term);
        button.setOnAction(this::toggleRegistration);

        VBox info = new VBox(5, date, address, capacity);
        HBox content = new HBox(20, info, button);

        VBox card = new VBox(10, title, content);
        card.setStyle("-fx-border-color: black; -fx-border-width: 2;");
        card.setPadding(new javafx.geometry.Insets(10));

        return card;
    }
    @FXML
    private void toggleRegistration(javafx.event.ActionEvent event) {

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
    }
}
