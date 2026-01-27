package sk.upjs.paz.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import sk.upjs.paz.app.SceneManager;
import sk.upjs.paz.model.District;
import sk.upjs.paz.model.User;
import sk.upjs.paz.service.DistrictService;
import sk.upjs.paz.service.RegisterService;

public class RegisterController {

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

        birthDatePicker.setEditable(false);

        birthDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(java.time.LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                if (empty || date.isAfter(java.time.LocalDate.now())) {
                    setDisable(true);
                }
            }
        });
    }

    @FXML
    private void createAccount() {

        String email = emailField.getText();
        String password = passwordField.getText();
        String confirm = confirmPasswordField.getText();
        String name = nameField.getText();
        String surname = surnameField.getText();
        String phone = phoneField.getText();

        if (email.isBlank() || password.isBlank()) {
            showAlert("Email and password are required");
            return;
        }

        if (!password.equals(confirm)) {
            showAlert("Passwords do not match");
            return;
        }

        if (!isValidEmail(email)) {
            showAlert("Email format is not valid");
            return;
        }

        if (containsDigit(name) || containsDigit(surname)) {
            showAlert("Name and surname must not contain digits");
            return;
        }

        if (birthDatePicker.getValue() == null) {
            showAlert("Please select date of birth");
            return;
        }

        if (!isValidPhone(phone)) {
            showAlert("Phone number must contain exactly 10 digits");
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

        String rc = personalNumberField.getText();

        if (registerService.personalNumberExists(rc)) {
            showAlert("User with this personal number already exists");
            return;
        }

        rc = personalNumberField.getText();

        if (!isValidPersonalNumber(rc)) {
            showAlert("Personal number is not valid");
            return;
        }

        User user = new User(
                name,
                surname,
                email,
                registerService.hashPassword(password),
                birthDatePicker.getValue(),
                selectedDistrict.getIdDistrict(),
                addressField.getText(),
                phone,
                personalNumberField.getText()
        );

        registerService.register(user);
        SceneManager.openLoginScene();
    }


    @FXML
    private void StepBackToLogin() {
        SceneManager.openLoginScene();
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    private boolean containsDigit(String text) {
        return text.matches(".*\\d.*");
    }

    private boolean isValidPhone(String phone) {
        return phone.matches("\\d{10}");
    }

    private boolean isValidPersonalNumber(String rc) {
        return rc != null && rc.matches("^\\d{6}/\\d{3,4}$");
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
