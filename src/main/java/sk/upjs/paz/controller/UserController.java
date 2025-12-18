package sk.upjs.paz.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import sk.upjs.paz.model.User;
import sk.upjs.paz.app.SceneManager;


import javafx.scene.image.ImageView;

public class UserController {

    @FXML private Label nameLabel;
    @FXML private Label surnameLabel;
    @FXML private Label birthDateLabel;
    @FXML private Label phoneLabel;
    @FXML private Label addressLabel;
    @FXML private Label personalNumberLabel;
    @FXML private ImageView mainPhoto;
    @FXML private ToggleButton themeToggle;
    @FXML private Button StatusButton;

    private static User currentUser = SceneManager.getCurrentUser();

    @FXML
    private void toggleTheme() {
        SceneManager.toggleTheme(themeToggle.getScene());

        themeToggle.setText(
                SceneManager.isDarkTheme() ? "☀ Light" : "🌙 Dark"
        );
    }

    @FXML
    private void initialize() {
        themeToggle.setText(
                SceneManager.isDarkTheme()
                        ? "☀ Light"
                        : "🌙 Dark"
        );
        applyRolePermissions();
    }

    @FXML
    private void openScheduleWindow() {//xx
        SceneManager.openScheduleWindow();
    }


    @FXML
    private void openMessageWindow(){
        SceneManager.openMessageWindow();
    }

    @FXML
    private void openStatusWindow()
    {
        SceneManager.openStatusWindow();
    }

    private void applyRolePermissions() {
        int role = currentUser.getRoleId();

        if (role == 3 || role == 2) {
            StatusButton.setDisable(true);
        }
    }

    private User user;

    public void setUser(User user) {
        this.user = user;
        initData();
    }

    private void initData() {
        if (user == null) {
            return;
        }

        nameLabel.setText(user.getName());
        surnameLabel.setText(user.getSurname());
        birthDateLabel.setText(user.getDateOfBirth().toString());
        phoneLabel.setText(user.getPhoneNumber());
        addressLabel.setText(user.getAddress());
        personalNumberLabel.setText(user.getPersonalNumber());
    }

    @FXML
    private void Logout() {
        SceneManager.openLoginScene();
    }

}
