package sk.upjs.paz.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sk.upjs.paz.model.User;
import sk.upjs.paz.service.UserService;
import sk.upjs.paz.app.SceneManager;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private final UserService userService = new UserService();

    @FXML
    private void Registration() {
       SceneManager.openRegistrationWindow();
    }


    @FXML
    private void initialize() {
        errorLabel.setText("");
    }

    @FXML
    private void login() {
        System.out.println("LOGIN BUTTON CLICKED");

        String email = emailField.getText();
        String password = passwordField.getText();

        User user = userService.authenticate(email, password);

        if (user == null) {
            System.out.println("AUTH FAILED");
            errorLabel.setText("Nesprávny email alebo heslo");
            return;
        }

        System.out.println("AUTH OK");
        SceneManager.openUserScene(user);
    }
}

