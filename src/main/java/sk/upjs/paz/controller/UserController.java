package sk.upjs.paz.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import sk.upjs.paz.dao.StatusHistoryDao;
import sk.upjs.paz.model.Status;
import sk.upjs.paz.model.User;
import sk.upjs.paz.app.SceneManager;


import javafx.scene.image.ImageView;
import sk.upjs.paz.service.StatusHistoryService;

public class UserController {

    @FXML private Label nameLabel;
    @FXML private Label surnameLabel;
    @FXML private Label birthDateLabel;
    @FXML private Label phoneLabel;
    @FXML private Label addressLabel;
    @FXML private Label personalNumberLabel;
    @FXML private Label StatusLabel;
    @FXML private ImageView mainPhoto;
    @FXML private ToggleButton themeToggle;
    @FXML private Button UserManagerButton;
    @FXML private Button ChangeDistrictButton;

    private final StatusHistoryService statusHistoryService =
            new StatusHistoryService();
    private User currentUser;

    @FXML
    private void toggleTheme() {
        SceneManager.toggleTheme(themeToggle.getScene());

        themeToggle.setText(
                SceneManager.isDarkTheme() ? "☀ Light" : "🌙 Dark"
        );
    }

    @FXML
    private void initialize()
    {
        currentUser = SceneManager.getCurrentUser();

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
    public void openStatusWindow() {
        SceneManager.openUserManagerWindow();
    }

    public void openChageDistrict() {
        SceneManager.openChangeDistrictWindow(currentUser);
    }

    private void applyRolePermissions() {
        int role = currentUser.getRoleId();

        if (role == 3) {
            UserManagerButton.setVisible(false);
            ChangeDistrictButton.setVisible(false);
        }
        if (role == 2) { ChangeDistrictButton.setVisible(false);}
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

        Status currentStatus = statusHistoryService.getCurrentStatus(user.getIdUser());

        if (currentStatus != null) {
            StatusLabel.setText(currentStatus.getName());
        } else {
            StatusLabel.setText("Neznámy stav");
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
