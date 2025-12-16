package sk.upjs.paz.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import sk.upjs.paz.app.SceneManager;
import sk.upjs.paz.model.Term;
import sk.upjs.paz.service.ScheduleService;

public class ScheduleController {

    @FXML
    private VBox termsContainer;

    private final ScheduleService scheduleService = new ScheduleService();

    @FXML
    private void initialize() {
        loadTerms();
    }

    private void loadTerms() {
        termsContainer.getChildren().clear();

        for (Term term : scheduleService.getAllTerms()) {
            termsContainer.getChildren().add(createTermCard(term));
        }
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
        button.setOnAction(e -> toggleRegistration(button, term));

        VBox info = new VBox(5, date, address, capacity);

        HBox content = new HBox(20, info, button);

        VBox card = new VBox(10, title, content);
        card.setStyle("-fx-border-color: black; -fx-border-width: 2;");
        card.setPadding(new javafx.geometry.Insets(10));

        return card;
    }

    private void toggleRegistration(Button button, Term term) {
        if ("Zapísať sa".equals(button.getText())) {
            button.setText("Odhlásiť sa");
            // TODO: INSERT INTO term_user
        } else {
            button.setText("Zapísať sa");
            // TODO: DELETE FROM term_user
        }
    }
}
