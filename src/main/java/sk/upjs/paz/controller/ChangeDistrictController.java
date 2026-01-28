package sk.upjs.paz.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import sk.upjs.paz.app.SceneManager;
import sk.upjs.paz.model.District;
import sk.upjs.paz.model.User;
import sk.upjs.paz.service.DistrictService;
import sk.upjs.paz.service.UserService;

public class ChangeDistrictController {

    @FXML private ComboBox<District> districtBox;

    private final DistrictService districtService = new DistrictService();
    private final UserService userService = new UserService();

    private User user;
    private final User currentUser = SceneManager.getCurrentUser();

    public void setUser(User user) {
        this.user = user;
        districtBox.getItems().setAll(districtService.getAllDistricts());
        districtBox.setValue(
                districtService.getDistrictById(user.getDistrictId())
        );
        applyPermissions();
    }

    private void applyPermissions() {
        if (currentUser == null || currentUser.getRoleId() == 3) {
            districtBox.setDisable(true);
        }
    }

    @FXML
    private void Zmenit() {
        SceneManager.openEditDistrictWindow(
                districtBox.getValue()
        );
    }

    @FXML
    private void back() {
        SceneManager.openUserScene(SceneManager.getCurrentUser());
    }

}
