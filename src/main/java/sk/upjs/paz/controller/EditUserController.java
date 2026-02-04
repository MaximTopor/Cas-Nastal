package sk.upjs.paz.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import sk.upjs.paz.app.SceneManager;
import sk.upjs.paz.model.District;
import sk.upjs.paz.model.User;
import sk.upjs.paz.service.DistrictService;
import sk.upjs.paz.service.UserService;

import java.util.Map;

public class EditUserController {

    private final UserService userService = new UserService();
    private final DistrictService districtService = new DistrictService();

    private User user;
    private final User currentUser = SceneManager.getCurrentUser();

    @FXML private TextField nameField;
    @FXML private TextField surnameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField addressField;

    @FXML private ComboBox<String> roleBox;
    @FXML private ComboBox<District> districtBox;

    // ===== ROLE MAPS =====
    private static final Map<Integer, String> ROLE_ID_TO_NAME = Map.of(
            1, "ADMIN",
            2, "OFFICER",
            3, "USER"
    );

    private static final Map<String, Integer> ROLE_NAME_TO_ID = Map.of(
            "ADMIN", 1,
            "OFFICER", 2,
            "USER", 3
    );

    @FXML
    private void initialize() {
        initRoleBox();
        initDistrictBox();
        applyRolePermissions();
    }

    public void setUser(User user) {
        this.user = user;
        fillForm();
    }

    private void initRoleBox() {
        roleBox.getItems().setAll("ADMIN", "OFFICER", "USER");
    }

    private void initDistrictBox() {
        districtBox.getItems().setAll(districtService.getAllDistricts());
    }

    private void applyRolePermissions() {
        if (currentUser == null || currentUser.getRoleId() != 1) {
            roleBox.setDisable(true); // тільки ADMIN може міняти роль
        }
    }

    private void fillForm() {
        if (user == null) return;

        nameField.setText(user.getName());
        surnameField.setText(user.getSurname());
        emailField.setText(user.getEmail());
        phoneField.setText(user.getPhoneNumber());
        addressField.setText(user.getAddress());

        roleBox.setValue(ROLE_ID_TO_NAME.get(user.getRoleId()));
        districtBox.setValue(
                districtService.getDistrictById(user.getDistrictId())
        );
    }

    @FXML
    private void save() {

        if (nameField.getText().isBlank()
                || surnameField.getText().isBlank()
                || emailField.getText().isBlank()
                || phoneField.getText().isBlank()
                || addressField.getText().isBlank()) {

            showError("Všetky povinné polia musia byť vyplnené.");
            return;
        }

        if (!emailField.getText().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            showError("Neplatný formát e-mailu.");
            return;
        }

        if (!phoneField.getText().matches("^\\+?[0-9]{9,15}$")) {
            showError("Neplatný formát telefónneho čísla.");
            return;
        }

        District selectedDistrict = districtBox.getValue();
        if (selectedDistrict == null) {
            showError("Musíte vybrať okres.");
            return;
        }

        if (userService.emailExistsForOtherUser(
                emailField.getText(),
                user.getIdUser()
        )) {
            showError("Tento e-mail už používa iný používateľ.");
            return;
        }

        if (userService.phoneExistsForOtherUser(
                phoneField.getText(),
                user.getIdUser()
        )) {
            showError("Toto telefónne číslo už používa iný používateľ.");
            return;
        }

        // ---------- 7. NAPLŇ USER ----------
        user.setName(nameField.getText());
        user.setSurname(surnameField.getText());
        user.setEmail(emailField.getText());
        user.setPhoneNumber(phoneField.getText());
        user.setAddress(addressField.getText());
        user.setDistrictId(selectedDistrict.getIdDistrict());

        if (currentUser != null && currentUser.getRoleId() == 1) {

            if (currentUser.getIdUser() == user.getIdUser()) {
                showError("Nemôžete zmeniť svoju vlastnú rolu.");
                return;
            }

            String selectedRole = roleBox.getValue();
            if (selectedRole != null) {
                user.setRoleId(ROLE_NAME_TO_ID.get(selectedRole));
            }
        }

        userService.update(user);
        SceneManager.openUserManagerWindow();
    }

    @FXML
    public void back() {
        SceneManager.openUserManagerWindow();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Chyba");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
