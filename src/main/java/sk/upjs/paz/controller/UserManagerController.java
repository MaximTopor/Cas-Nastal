package sk.upjs.paz.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import sk.upjs.paz.app.SceneManager;
import sk.upjs.paz.model.District;
import sk.upjs.paz.model.Status;
import sk.upjs.paz.model.User;
import sk.upjs.paz.service.DistrictService;
import sk.upjs.paz.service.StatusHistoryService;
import sk.upjs.paz.service.UserService;

import java.util.List;

public class UserManagerController {

    @FXML private ListView<User> userListView;
    @FXML private ComboBox<District> districtFilterBox;
    @FXML private TextField personalNumberSearchField;

    private final DistrictService districtService = new DistrictService();
    private final UserService userService = new UserService();
    private final StatusHistoryService statusHistoryService = new StatusHistoryService();

    private List<Status> allStatuses;

    @FXML
    private void initialize() {
        userListView.setFocusTraversable(false);

        // --- DEBUG ---
        userListView.getItems().addListener((javafx.collections.ListChangeListener<User>) c -> {
            System.out.println("ITEMS CHANGED: size=" + userListView.getItems().size());
        });
        userListView.getSelectionModel().selectedIndexProperty().addListener((obs, o, n) -> {
            System.out.println("SELECTED INDEX = " + n);
        });

        // --- WORKAROUND for JavaFX ListViewBehavior mousePressed crash ---
        userListView.addEventFilter(javafx.scene.input.MouseEvent.MOUSE_PRESSED, e -> {
            // не блокуємо кліки по ComboBox всередині клітинки
            javafx.scene.Node n = e.getPickResult().getIntersectedNode();
            while (n != null) {
                if (n instanceof javafx.scene.control.ComboBox<?>) {
                    return;
                }
                n = n.getParent();
            }
            e.consume();
        });

        userListView.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_RELEASED, e -> {
            if (userListView.getItems().isEmpty()) return;

            javafx.scene.Node n = e.getPickResult().getIntersectedNode();
            while (n != null && !(n instanceof javafx.scene.control.ListCell<?>)) {
                n = n.getParent();
            }

            if (n instanceof javafx.scene.control.ListCell<?> cell) {
                int idx = cell.getIndex();
                if (idx >= 0 && idx < userListView.getItems().size()) {
                    userListView.getSelectionModel().clearAndSelect(idx);
                }
            }
        });

        // --- DATA ---
        allStatuses = statusHistoryService.getAllStatuses();

        initDistrictFilter();
        loadUsers();
        userListView.setCellFactory(lv -> new UserCell());
    }


    private final class UserCell extends ListCell<User> {
        private final HBox root = new HBox(15);
        private final Label nameLabel = new Label();
        private final ComboBox<Status> statusBox = new ComboBox<>();

        private User cellUser;
        private Status currentStatus;
        private boolean updating = false;

        private UserCell() {
            root.setAlignment(Pos.CENTER_LEFT);
            root.getChildren().addAll(nameLabel, statusBox);

            nameLabel.getStyleClass().add("user-name");
            statusBox.getStyleClass().add("user-status-combo");
            statusBox.setPrefWidth(160);
            statusBox.setItems(FXCollections.observableArrayList(allStatuses));

            statusBox.setOnAction(e -> {
                if (updating) return;
                if (cellUser == null) return;

                Status selected = statusBox.getValue();
                if (selected == null) return;
                if (currentStatus != null && selected.getIdStatus() == currentStatus.getIdStatus()) return;

                userListView.getSelectionModel().select(cellUser);

                Platform.runLater(() -> {
                    SceneManager.openChangeStatusWindow(cellUser, currentStatus, selected);
                    reloadCurrentList();
                });

                updating = true;
                try {
                    statusBox.setValue(currentStatus);
                } finally {
                    updating = false;
                }
            });
        }

        @Override
        protected void updateItem(User user, boolean empty) {
            super.updateItem(user, empty);

            if (empty || user == null) {
                cellUser = null;
                currentStatus = null;
                setGraphic(null);
                return;
            }

            cellUser = user;
            nameLabel.setText(user.getName() + " " + user.getSurname());

            currentStatus = statusHistoryService.getCurrentStatus(user.getIdUser());

            updating = true;
            try {
                statusBox.setValue(currentStatus);
            } finally {
                updating = false;
            }

            setGraphic(root);
        }
    }

    @FXML
    private void searchByPersonalNumber() {
        String personalNumber = personalNumberSearchField.getText();

        userListView.getSelectionModel().clearSelection();

        if (personalNumber == null || personalNumber.isBlank()) {
            loadUsers();
            return;
        }

        User user = userService.getUserByPersonalNumber(personalNumber);

        if (user != null) {
            userListView.getItems().setAll(user);
        } else {
            userListView.getItems().clear();
        }
    }

    private void initDistrictFilter() {
        districtFilterBox.getItems().setAll(districtService.getAllDistricts());
        districtFilterBox.setPromptText("Všetky okresy");
        districtFilterBox.setOnAction(e -> filterUsers());
    }

    private void filterUsers() {
        District selected = districtFilterBox.getValue();

        userListView.getSelectionModel().clearSelection();

        if (selected == null) {
            loadUsers();
        } else {
            userListView.getItems().setAll(
                    userService.getUsersByDistrict(selected.getIdDistrict())
            );
        }
    }

    private void loadUsers() {
        var all = userService.getAllUsers();
        System.out.println("USERS COUNT = " + all.size());
        userListView.getSelectionModel().clearSelection();
        userListView.getItems().setAll(userService.getAllUsers());
    }

    private void reloadCurrentList() {
        String pn = personalNumberSearchField.getText();
        District d = districtFilterBox.getValue();

        if (pn != null && !pn.isBlank()) {
            searchByPersonalNumber();
            return;
        }
        if (d != null) {
            filterUsers();
            return;
        }
        loadUsers();
    }

    @FXML
    private void goNext() {
        User selectedUser = userListView.getSelectionModel().getSelectedItem();
        if (selectedUser == null) return;
        SceneManager.openUserEditWindow(selectedUser);
    }

    @FXML
    private void goBack() {
        SceneManager.openUserScene(SceneManager.getCurrentUser());
    }
}
