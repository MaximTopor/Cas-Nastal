package sk.upjs.paz.controller;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sk.upjs.paz.app.I18n;
import sk.upjs.paz.dao.StatusHistoryDao;
import sk.upjs.paz.model.Status;
import sk.upjs.paz.model.User;
import sk.upjs.paz.app.SceneManager;


import javafx.scene.image.ImageView;
import sk.upjs.paz.service.StatusHistoryService;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

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


    @FXML private AnchorPane root;

    private static final double BASE_W = 1400.0;
    private static final double BASE_H = 600.0;

    @FXML
    private ComboBox<String> languageBox;


    @FXML
    private void onLanguageChanged() {
        String v = languageBox.getValue();
        if (v == null) return;

        Locale newLocale = v.equals("UK") ? new Locale("uk") : new Locale("sk");
        if (newLocale.equals(I18n.getLocale())) return;

        I18n.setLocale(newLocale);

        try {
            FXMLLoader loader = I18n.loader("/views/user.fxml");
            Parent newRoot = loader.load();

            UserController newController = loader.getController();
            newController.setUser(this.user);

            // заміна root без зміни SceneManager
            languageBox.getScene().setRoot(newRoot);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }




    @FXML
    private void toggleTheme() {
        SceneManager.toggleTheme(themeToggle.getScene());

        themeToggle.setText(
                SceneManager.isDarkTheme() ? "☀ Light" : "🌙 Dark"
        );
    }



    @FXML
    private void initialize() {

//        root.sceneProperty().addListener((obs, oldScene, scene) -> {
//            if (scene == null) return;
//
//            // Коли з'явиться Stage — ставимо мінімальні розміри
//            scene.windowProperty().addListener((o, oldW, w) -> {
//                if (w instanceof Stage stage) {
//                    stage.setMinWidth(BASE_W);
//                    stage.setMinHeight(BASE_H);
//                }
//            });
//
//            // Масштабування при зміні розміру вікна
//            scene.widthProperty().addListener((o, ov, nv) -> applyScale(scene));
//            scene.heightProperty().addListener((o, ov, nv) -> applyScale(scene));
//
//            applyScale(scene);
//        });

        if (languageBox != null) {
            languageBox.getItems().setAll("SK", "UK");
            languageBox.setValue(I18n.getLocale().getLanguage().equals("uk") ? "UK" : "SK");
            languageBox.setOnAction(e -> onLanguageChanged());
        }
        currentUser = SceneManager.getCurrentUser();

        themeToggle.setText(
                SceneManager.isDarkTheme()
                        ? "☀ Light"
                        : "🌙 Dark"
        );
        applyRolePermissions();
    }

    private void applyScale(Scene scene) {
        double sx = scene.getWidth() / BASE_W;
        double sy = scene.getHeight() / BASE_H;

        // Масштабуємо пропорційно, щоб не розтягувати по-різному
        double s = Math.min(sx, sy);

        // Не зменшуємо нижче 1.0, бо мінімальний розмір і так 1400x600
        if (s < 1.0) s = 1.0;

        root.setScaleX(s);
        root.setScaleY(s);
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
