package sk.upjs.paz.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;

import sk.upjs.paz.app.SceneManager;
import sk.upjs.paz.model.District;
import sk.upjs.paz.model.Status;
import sk.upjs.paz.model.User;
import sk.upjs.paz.service.DistrictService;
import sk.upjs.paz.service.StatusHistoryService;
import sk.upjs.paz.service.UserService;

public class UserManagerController {

    @FXML
    private ListView<User> userListView;

    @FXML
    private ComboBox<District> districtFilterBox;

    @FXML
    private TextField personalNumberSearchField;

    private final DistrictService districtService = new DistrictService();
    private final UserService userService = new UserService();
    private final StatusHistoryService statusHistoryService = new StatusHistoryService();

    @FXML
    private void initialize() {

        userListView.setFocusTraversable(false);

        initDistrictFilter();
        loadUsers();

        userListView.setCellFactory(list -> new ListCell<>() {

            private final HBox root = new HBox(15);
            private final Label nameLabel = new Label();
            private final ComboBox<Status> statusBox = new ComboBox<>();

            private Status currentStatus;

            {
                root.setAlignment(Pos.CENTER_LEFT);
                root.getChildren().addAll(nameLabel, statusBox);

                nameLabel.getStyleClass().add("user-name");
                statusBox.getStyleClass().add("user-status-combo");

                statusBox.setPrefWidth(160);
                statusBox.setItems(
                        FXCollections.observableArrayList(
                                statusHistoryService.getAllStatuses()
                        )
                );
            }

            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);

                if (empty || user == null) {
                    setGraphic(null);
                    return;
                }


                nameLabel.setText(
                        user.getName() + " " + user.getSurname()
                );


                currentStatus = statusHistoryService
                        .getCurrentStatus(user.getIdUser());

                statusBox.setValue(currentStatus);

                statusBox.setOnAction(e -> {
                    Status selected = statusBox.getValue();

                    if (selected == null) return;
                    if (selected.getIdStatus() == currentStatus.getIdStatus()) return;

                    javafx.application.Platform.runLater(() -> {
                        SceneManager.openChangeStatusWindow(
                                user,
                                currentStatus,
                                selected
                        );
                    });

                    statusBox.setValue(currentStatus);
                });

                setGraphic(root);
            }
        });
    }

    @FXML
    private void searchByPersonalNumber() {

        String personalNumber = personalNumberSearchField.getText();

        javafx.application.Platform.runLater(() -> {
            userListView.getSelectionModel().clearSelection();

            if (personalNumber == null || personalNumber.isBlank()) {
                loadUsers();
                return;
            }

            User user = userService.getUserByPersonalNumber(personalNumber);

            if (user != null) {
                userListView.getItems().setAll(user);
            } else {
                userListView.getItems().setAll();
            }
        });
    }


    private void initDistrictFilter() {

        districtFilterBox.getItems().setAll(
                districtService.getAllDistricts()
        );

        districtFilterBox.setPromptText("Všetky okresy");

        districtFilterBox.setOnAction(e -> filterUsers());
    }

    private void filterUsers() {

        District selected = districtFilterBox.getValue();

        if (selected == null) {
            loadUsers();
        } else {
            userListView.getItems().setAll(
                    userService.getUsersByDistrict(
                            selected.getIdDistrict()
                    )
            );
        }
    }

    private void loadUsers() {
        userListView.getSelectionModel().clearSelection();
        userListView.getItems().setAll(
                userService.getAllUsers()
        );
    }

    @FXML
    private void goNext() {

        User selectedUser =
                userListView.getSelectionModel()
                        .getSelectedItem();

        if (selectedUser == null) {
            return;
        }

        SceneManager.openUserEditWindow(selectedUser);
    }

    @FXML
    private void goBack() {
        SceneManager.openUserScene(SceneManager.getCurrentUser());
    }
}
