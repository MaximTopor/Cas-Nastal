package sk.upjs.paz.app;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sk.upjs.paz.controller.UserController;
import sk.upjs.paz.model.User;

import java.awt.*;

public class SceneManager {

    private static boolean darkTheme = true;
    private static Stage acStage;
    private static User currentUser;

    /* ================= INIT ================= */

    public static void setStage(Stage stage) {
        acStage = stage;
    }

    public static boolean isDarkTheme() {
        return darkTheme;
    }

    /* ================= SCENES ================= */

    public static void openLoginScene() {
        switchTo("/views/login.fxml", "Login");
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

            Scene scene = new Scene(root);
            applyTheme(scene);

            acStage.setScene(scene);
            acStage.setTitle("User panel");
            acStage.setResizable(false);
            acStage.show();

        } catch (Exception e) {
            throw new RuntimeException("Cannot load user scene", e);
        }
    }

    public static void openTermWindow() {
        switchTo("/views/schedule.fxml", "Term");
    }


    public static void backToProfile() {
        if (currentUser != null) {
            openUserScene(currentUser);
        }
    }

    public static void openRegistrationWindow() {
        switchTo("/views/Registration.fxml", "Registration");
    }

    public static User getCurrentUser() {
        if (currentUser == null) {
            throw new IllegalStateException(
                    "Current user is not set. Did you forget to call setCurrentUser() after login?"
            );
        }
        return currentUser;
    }

    public static void openMessageWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    SceneManager.class.getResource("/views/message.fxml")
            );
            Parent root = loader.load();

            Scene scene = new Scene(root);
            applyTheme(scene);

            Stage messageStage = new Stage();
            messageStage.setTitle("Messages");
            messageStage.setScene(scene);
            messageStage.setResizable(false);
            messageStage.initOwner(acStage);
            messageStage.initModality(Modality.WINDOW_MODAL);

            messageStage.showAndWait();

        } catch (Exception e) {
            throw new RuntimeException("Cannot open message window", e);
        }
    }

    public static void toggleTheme(Scene scene) {
        darkTheme = !darkTheme;
        applyTheme(scene);
    }

    private static void applyTheme(Scene scene) {
        scene.getStylesheets().clear();

        String css = darkTheme
                ? "/css/dark.css"
                : "/css/light.css";

        var url = SceneManager.class.getResource(css);
        if (url == null) {
            throw new IllegalStateException("CSS not found: " + css);
        }

        scene.getStylesheets().add(url.toExternalForm());
    }

    /* ================= CORE ================= */

    private static void switchTo(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    SceneManager.class.getResource(fxmlPath)
            );
            Parent root = loader.load();

            Scene scene = new Scene(root);
            applyTheme(scene);

            acStage.setScene(scene);
            acStage.setTitle(title);
            acStage.setResizable(false);
            acStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}