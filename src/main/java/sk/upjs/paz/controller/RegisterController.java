package sk.upjs.paz.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import sk.upjs.paz.app.SceneManager;

public class RegisterController {

    @FXML
    private VBox stepOnePane;

    @FXML
    private GridPane stepTwoPane;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private void StepBackToLogin()
    {
        SceneManager.openLoginScene();
    }
}
