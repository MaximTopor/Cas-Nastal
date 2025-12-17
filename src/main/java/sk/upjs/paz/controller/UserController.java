package sk.upjs.paz.controller;

import javafx.fxml.FXML;
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

    @FXML
    private void toggleTheme() {
        SceneManager.toggleTheme();
        themeToggle.setText(
                SceneManager.isDarkTheme() ? "🌙 Dark" : "☀ Light"
        );
    }

    @FXML
    private void openScheduleWindow() {
        SceneManager.openScheduleWindow();
    }

    @FXML
    private void openMessageWindow(){
        SceneManager.openMessageWindow();
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
    private void logout() {
        SceneManager.openLoginScene();
    }

}
