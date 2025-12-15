package sk.upjs.paz.app;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sk.upjs.paz.controller.UserController;
import sk.upjs.paz.model.User;

public class SceneManager {

    private static Stage acStage;


    public static void setStage(Stage stage) {
        acStage = stage;
    }

    public static void openLoginScene() {
        switchTo("login.fxml", "Login");
    }

    public static void openUserScene(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    SceneManager.class.getResource("/views/user.fxml")
            );

            Parent root = loader.load();

            UserController controller = loader.getController();
            controller.setUser(user);

            acStage.setScene(new Scene(root));
            acStage.setTitle("User panel");
            acStage.setResizable(false);
            acStage.show();

        } catch (Exception e) {
            throw new RuntimeException("Cannot load user scene", e);
        }
    }

    private static void switchTo(String fxmlName, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    SceneManager.class.getResource("/views/" + fxmlName)
            );

            Parent root = loader.load();
            Scene scene = new Scene(root);

            acStage.setScene(scene);
            acStage.setTitle(title);
            acStage.setResizable(false);
            acStage.show();

        } catch (Exception e) {
            System.err.println("Error loading FXML: " + fxmlName);
            e.printStackTrace();
        }
    }
}
