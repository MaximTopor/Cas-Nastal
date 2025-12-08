package sk.upjs.paz.controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sk.upjs.paz.app.SceneManager;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    public void login() {
        String email = emailField.getText();
        String pass = passwordField.getText();

        System.out.println("Login attempt: " + email);

        // TODO: підключення до DB і перевірка паролю
        SceneManager.switchTo("dashboard.fxml", "Dashboard");
    }
}
