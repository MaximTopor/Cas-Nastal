package sk.upjs.paz.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import sk.upjs.paz.app.SceneManager;
import sk.upjs.paz.model.District;
import sk.upjs.paz.model.User;
import sk.upjs.paz.service.DistrictService;

public class EditDistrictController {

    @FXML private TextField nameField;
    @FXML private TextField addressField;
    @FXML private TextField contactField;
    @FXML private TextField postalCodeField;
    @FXML private TextField regionField;

    private District district;
    private final User currentUser = SceneManager.getCurrentUser();
    private final DistrictService districtService = new DistrictService();

    public void setDistrict(District district) {
        this.district = district;
        fillForm();
    }

    private void fillForm() {
        if (district == null) return;

        nameField.setText(district.getName());
        addressField.setText(district.getAddressOfCenter());
        contactField.setText(district.getContact());
        postalCodeField.setText(String.valueOf(district.getPostalCode()));
        regionField.setText(district.getRegion());
    }

    @FXML
    private void save() {
        if (nameField.getText().isBlank()
                || addressField.getText().isBlank()
                || contactField.getText().isBlank()
                || postalCodeField.getText().isBlank()
                || regionField.getText().isBlank()) {

            showError("Všetky polia sú povinné.");
            return;
        }

        int psc;
        try {
            psc = Integer.parseInt(postalCodeField.getText());
        } catch (NumberFormatException e) {
            showError("PSČ musí byť číslo.");
            return;
        }

        try {
            district.setName(nameField.getText().trim());
            district.setAddressOfCenter(addressField.getText().trim());
            district.setContact(contactField.getText().trim());
            district.setPostalCode(psc);
            district.setRegion(regionField.getText().trim());

            districtService.updateDistrict(currentUser, district);

            SceneManager.openChangeDistrictWindow(currentUser);

        } catch (SecurityException e) {
            showError("Nemáte oprávnenie meniť okres.");

        } catch (IllegalArgumentException e) {

            switch (e.getMessage()) {
                case "NAME_EXISTS" ->
                        showError("Okres s týmto názvom už existuje.");
                case "ADDRESS_EXISTS" ->
                        showError("Táto adresa centra už existuje.");
                case "PSC_EXISTS" ->
                        showError("PSČ už existuje.");
                default ->
                        showError("Neznáma chyba.");
            }
        }
    }

    @FXML
    private void back() {
        SceneManager.openChangeDistrictWindow(currentUser);
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
