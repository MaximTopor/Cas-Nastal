package sk.upjs.paz.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        SceneManager.setStage(stage);
        SceneManager.openLoginScene();
    }

    public static void main(String[] args) {
        launch();
    }   //статус + контро111лер + scc до термінів
}