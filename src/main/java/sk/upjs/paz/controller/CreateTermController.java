package sk.upjs.paz.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sk.upjs.paz.app.I18n;
import sk.upjs.paz.app.SceneManager;
import sk.upjs.paz.model.District;
import sk.upjs.paz.model.Term;
import sk.upjs.paz.service.CreateTermService;
import sk.upjs.paz.service.DistrictService;

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
        regionComboBox.getItems().setAll(districtService.getAllDistricts());
        datePicker.setEditable(false);

        // i18n texts (НЕ перезаписуй FXML-ключі звичайним текстом)
        applyModeTexts();

        // Theme CSS (як у тебе було)
        Scene scene = titleLabel.getScene();
        if (scene != null) {
            applyCreateTermCss(scene);
        } else {
            titleLabel.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) applyCreateTermCss(newScene);
            });
        }
    }

    public void setTermToEdit(Term term) {
        if (term.getDate().isBefore(LocalDate.now())) {
            showError(I18n.bundle().getString("createTerm.err.pastTerm"));
            closeWindow();
            return;
        }

        this.mode = Mode.EDIT;
        this.editedTerm = term;

        applyModeTexts();
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
                        Integer.parseInt(capacityField.getText().trim()),
                        regionComboBox.getValue().getIdDistrict()
                );
                showInfo(I18n.bundle().getString("createTerm.successCreated"));
            } else {
                termService.updateTerm(
                        editedTerm.getIdTerms(),
                        typeField.getText().trim(),
                        datePicker.getValue(),
                        parseTime(startTimeField.getText()),
                        parseTime(endTimeField.getText()),
                        addressField.getText().trim(),
                        Integer.parseInt(capacityField.getText().trim()),
                        regionComboBox.getValue().getIdDistrict()
                );
                showInfo(I18n.bundle().getString("createTerm.successUpdated"));
            }

            closeWindow();

        } catch (Exception e) {
            // якщо це твоя помилка з validateForm/parseTime — там вже i18n-тексти
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
        if (typeField.getText() == null || typeField.getText().isBlank()) {
            throw new IllegalArgumentException(I18n.bundle().getString("createTerm.err.type"));
        }
        if (datePicker.getValue() == null) {
            throw new IllegalArgumentException(I18n.bundle().getString("createTerm.err.date"));
        }
        if (regionComboBox.getValue() == null) {
            throw new IllegalArgumentException(I18n.bundle().getString("createTerm.err.district"));
        }
        if (addressField.getText() == null || addressField.getText().isBlank()) {
            throw new IllegalArgumentException(I18n.bundle().getString("createTerm.err.address"));
        }
        if (capacityField.getText() == null || capacityField.getText().isBlank()) {
            throw new IllegalArgumentException(I18n.bundle().getString("createTerm.err.capacity"));
        }
        // додатково: перевірка що capacity число
        try {
            Integer.parseInt(capacityField.getText().trim());
        } catch (Exception e) {
            throw new IllegalArgumentException(I18n.bundle().getString("createTerm.err.capacity"));
        }
    }

    private LocalTime parseTime(String value) {
        try {
            return LocalTime.parse(value.trim()); // HH:mm
        } catch (Exception e) {
            throw new IllegalArgumentException(I18n.bundle().getString("createTerm.err.timeFormat"));
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(I18n.bundle().getString("common.error"));
        alert.setHeaderText(I18n.bundle().getString("common.operationFailed"));
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(I18n.bundle().getString("common.done"));
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void applyModeTexts() {
        if (mode == Mode.EDIT) {
            titleLabel.setText(I18n.bundle().getString("createTerm.editTitle"));
            createButton.setText(I18n.bundle().getString("createTerm.saveChanges"));
        } else {
            titleLabel.setText(I18n.bundle().getString("createTerm.title"));
            createButton.setText(I18n.bundle().getString("createTerm.create"));
        }
    }

    private void applyCreateTermCss(Scene scene) {
        scene.getStylesheets().setAll(
                getClass().getResource(
                        SceneManager.isDarkTheme()
                                ? "/css/create-term-dark.css"
                                : "/css/create-term-light.css"
                ).toExternalForm()
        );
    }
}
