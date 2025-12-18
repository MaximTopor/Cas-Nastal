package sk.upjs.paz.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import sk.upjs.paz.app.SceneManager;
import sk.upjs.paz.model.District;
import sk.upjs.paz.model.User;
import sk.upjs.paz.service.DistrictService;
import sk.upjs.paz.service.RegisterService;

public class RegisterController {

    private static final int DEFAULT_ROLE_ID = 2; // USER

    private final RegisterService registerService = new RegisterService();
    private final DistrictService districtService = new DistrictService();

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;

    @FXML private TextField nameField;
    @FXML private TextField surnameField;
    @FXML private DatePicker birthDatePicker;
    @FXML private TextField personalNumberField;
    @FXML private ComboBox<District> regionComboBox;

    @FXML private TextField addressField;
    @FXML private TextField phoneField;

    @FXML
    private void initialize() {
        regionComboBox.getItems().setAll(
                districtService.getAllDistricts()
        );
    }

    @FXML
    private void createAccount() {

        String email = emailField.getText();
        String password = passwordField.getText();
        String confirm = confirmPasswordField.getText();

        if (email.isBlank() || password.isBlank()) {
            showAlert("Email and password are required");
            return;
        }

        if (!password.equals(confirm)) {
            showAlert("Passwords do not match");
            return;
        }

        if (registerService.emailExists(email)) {
            showAlert("User with this email already exists");
            return;
        }

        District selectedDistrict = regionComboBox.getValue();
        if (selectedDistrict == null) {
            showAlert("Please select district");
            return;
        }

        User user = new User(
                nameField.getText(),
                surnameField.getText(),
                email,
                registerService.hashPassword(password),
                birthDatePicker.getValue(),
                selectedDistrict.getIdDistrict(),
                addressField.getText(),
                phoneField.getText(),
                personalNumberField.getText()

        );

        registerService.register(user);
        SceneManager.openLoginScene();
    }

    @FXML
    private void StepBackToLogin() {
        SceneManager.openLoginScene();
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
