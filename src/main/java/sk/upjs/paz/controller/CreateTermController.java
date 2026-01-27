package sk.upjs.paz.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sk.upjs.paz.model.District;
import sk.upjs.paz.model.Term;
import sk.upjs.paz.service.CreateTermService;
import sk.upjs.paz.service.DistrictService;
import sk.upjs.paz.app.SceneManager;

import java.time.LocalDate;
import java.time.LocalTime;

public class CreateTermController {



    private enum Mode {
        CREATE,
        EDIT
    }
    private Mode mode = Mode.CREATE;
    private Term editedTerm;

    @FXML private Label titleLabel;
    @FXML private TextField typeField;
    @FXML private DatePicker datePicker;
    @FXML private TextField startTimeField;
    @FXML private TextField endTimeField;
    @FXML private TextField addressField;
    @FXML private TextField capacityField;
    @FXML private ComboBox<District> regionComboBox;
    @FXML private Button createButton;

    private final CreateTermService termService = new CreateTermService();
    private final DistrictService districtService = new DistrictService();

    @FXML
    private void initialize() {
        regionComboBox.getItems().setAll(
                districtService.getAllDistricts()
        );

        datePicker.setEditable(false);

        titleLabel.setText("Create term");
        createButton.setText("Create");



        Scene scene = titleLabel.getScene();
        if (scene != null) {
            scene.getStylesheets().setAll(
                    getClass().getResource(
                            SceneManager.isDarkTheme()
                                    ? "/css/create-term-dark.css"
                                    : "/css/create-term-light.css"
                    ).toExternalForm()
            );
        } else {
            titleLabel.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    newScene.getStylesheets().setAll(
                            getClass().getResource(
                                    SceneManager.isDarkTheme()
                                            ? "/css/create-term-dark.css"
                                            : "/css/create-term-light.css"
                            ).toExternalForm()
                    );
                }
            });
        }

    }



    public void setTermToEdit(Term term) {

        if (term.getDate().isBefore(LocalDate.now())) {
            showError(
                    "Tento termín je v minulosti a nie je možné ho upravovať."
            );
            closeWindow();
            return;
        }

        this.mode = Mode.EDIT;
        this.editedTerm = term;

        titleLabel.setText("Edit term");
        createButton.setText("Save changes");

        fillForm(term);
    }

    @FXML
    private void onCreate() {
        try {
            validateForm();

            if (mode == Mode.CREATE) {
                termService.createTerm(
                        typeField.getText().trim(),
                        datePicker.getValue(),
                        parseTime(startTimeField.getText()),
                        parseTime(endTimeField.getText()),
                        addressField.getText().trim(),
                        Integer.parseInt(capacityField.getText()),
                        regionComboBox.getValue().getIdDistrict()
                );
                showInfo("Termín bol úspešne vytvorený.");
            } else {
                termService.updateTerm(
                        editedTerm.getIdTerms(),
                        typeField.getText().trim(),
                        datePicker.getValue(),
                        parseTime(startTimeField.getText()),
                        parseTime(endTimeField.getText()),
                        addressField.getText().trim(),
                        Integer.parseInt(capacityField.getText()),
                        regionComboBox.getValue().getIdDistrict()
                );
                showInfo("Termín bol úspešne upravený.");
            }

            closeWindow();

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void backToSchedule() {
        closeWindow();
        SceneManager.openScheduleWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) createButton.getScene().getWindow();
        stage.close();
    }

    private void fillForm(Term term) {
        typeField.setText(term.getType());
        datePicker.setValue(term.getDate());
        startTimeField.setText(term.getStartTime().toString());
        endTimeField.setText(term.getEndTime().toString());
        addressField.setText(term.getAddress());
        capacityField.setText(String.valueOf(term.getCapacity()));

        regionComboBox.getSelectionModel().select(
                districtService.getDistrictById(term.getDistrictId())
        );
    }

    private void validateForm() {
        if (typeField.getText().isBlank()) {
            throw new IllegalArgumentException("Zadajte typ termínu");
        }
        if (datePicker.getValue() == null) {
            throw new IllegalArgumentException("Vyberte dátum");
        }
        if (regionComboBox.getValue() == null) {
            throw new IllegalArgumentException("Vyberte okres");
        }
        if (addressField.getText().isBlank()) {
            throw new IllegalArgumentException("Zadajte adresu");
        }
        if (capacityField.getText().isBlank()) {
            throw new IllegalArgumentException("Zadajte kapacitu");
        }
    }

    private LocalTime parseTime(String value) {
        try {
            return LocalTime.parse(value); // HH:mm
        } catch (Exception e) {
            throw new IllegalArgumentException("Čas musí byť vo formáte HH:mm");
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Chyba");
        alert.setHeaderText("Operácia zlyhala");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hotovo");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
