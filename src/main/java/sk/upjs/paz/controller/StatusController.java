package sk.upjs.paz.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import sk.upjs.paz.app.SceneManager;

import sk.upjs.paz.model.User;
import sk.upjs.paz.service.UserService;

public class StatusController
{

    @FXML
    private ListView<User> userListView;

    private final UserService userService = new UserService();


    @FXML
    private void initialize() {

        System.out.println("USERS = " + userService.getAllUsers().size());
        userListView.setItems(FXCollections.observableArrayList(userService.getAllUsers()));

        userListView.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);

                if (empty || user == null) {
                    setText(null);
                } else {
                    setText(user.getName() + " " + user.getSurname());
                }
            }
        });
    }


    @FXML
    private void goNext() {

        User selectedUser = userListView.getSelectionModel().getSelectedItem();

        if (selectedUser == null) {
            return;
        }

//        SceneManager.openUserEditWindow(selectedUser);

    }

    @FXML
    private void goBack() {
        SceneManager.backToProfile();
    }




}
