package sk.upjs.paz.app;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sk.upjs.paz.controller.UserController;
import sk.upjs.paz.model.User;

public class SceneManager {

    private static Stage acStage;
    private static User currentUser;

    public static void setStage(Stage stage) {
        acStage = stage;
    }

    public static void openTermWindow() {
        switchTo("schedule.fxml", "Terms");
    }

    public static void backToProfile() {
        openUserScene(currentUser);
    }

    public static void openMessageWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    SceneManager.class.getResource("/views/message.fxml")
            );
            Parent root = loader.load();

            Stage messageStage = new Stage();
            messageStage.setTitle("Messages");
            messageStage.setScene(new Scene(root));
            messageStage.setResizable(false);

            // 🔒 МОДАЛЬНІСТЬ
            messageStage.initOwner(acStage);                // блокує саме Profile
            messageStage.initModality(Modality.WINDOW_MODAL); // поки не закриють

            messageStage.showAndWait(); // ⛔ блокує виконання

        } catch (Exception e) {
            throw new RuntimeException("Cannot open message window", e);
        }
    }


    public static void openLoginScene() {
        switchTo("login.fxml", "Login");
    }

    public static void openUserScene(User user) {
        currentUser = user;
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

    private static void openWindow(String fxml, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    SceneManager.class.getResource("/views/" + fxml)
            );
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();

        } catch (Exception e) {
            throw new RuntimeException("Cannot open window: " + fxml, e);
        }
    }
}
