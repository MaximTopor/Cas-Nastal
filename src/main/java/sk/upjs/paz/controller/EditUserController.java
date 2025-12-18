package sk.upjs.paz.controller;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import sk.upjs.paz.app.SceneManager;
import sk.upjs.paz.model.User;

public class EditUserController
{
    private User user;

    public void setUser(User user) {
        this.user = user;
        fillForm(); // ← одразу заповнюємо
    }

    private void fillForm() {
        if (user == null) return;
//
//        nameField.setText(user.getName());
//        surnameField.setText(user.getSurname());
//        emailField.setText(user.getEmail());
//        phoneField.setText(user.getPhoneNumber());
//        addressField.setText(user.getAddress());
//
//        roleBox.setValue(resolveRoleName(user.getRoleId()));
//        districtBox.setValue(resolveDistrictName(user.getDistrictId()));
    }

    @FXML
    public void back()
    {
        SceneManager.openStatusWindow();
    }

    @FXML
    private void save() {

//        user.setName(nameField.getText());
//        user.setSurname(surnameField.getText());
//        user.setEmail(emailField.getText());
//        user.setPhoneNumber(phoneField.getText());
//        user.setAddress(addressField.getText());
//
//        userService.update(user);
//
//        ((Stage) nameField.getScene().getWindow()).close();
    }


}
