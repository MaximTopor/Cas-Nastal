package sk.upjs.paz.controller;

import javafx.scene.Node;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;

public class MessageController {

    private void close(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}
