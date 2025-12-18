package sk.upjs.paz.app;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sk.upjs.paz.controller.CreateTermController;
import sk.upjs.paz.controller.UserController;
import sk.upjs.paz.model.Term;
import sk.upjs.paz.model.User;

import java.net.URL;

public class SceneManager {

    private static Stage primaryStage;
    private static User currentUser;
    private static boolean darkTheme = true;

    /* ================= THEME ================= */

    public enum Theme {
        LIGHT, DARK
    }

    private static Theme currentTheme = Theme.LIGHT;

    private static String themeCss() {
        return currentTheme == Theme.DARK
                ? "/css/theme-dark.css"
                : "/css/theme-light.css";
    }

    public static void toggleTheme(Scene scene) {
        currentTheme = (currentTheme == Theme.DARK)
                ? Theme.LIGHT
                : Theme.DARK;

        if (activeStage != null && activeStage.getScene() != null) {
            String windowCss =
                    (String) activeStage.getScene().getRoot()
                            .getProperties().get("windowCss");

            applyStyles(activeStage.getScene(), windowCss);
        }
    }

    /* ================= STATE ================= */

    private static Stage activeStage;

    public static void setStage(Stage stage) {
        activeStage = stage;
    }

    public static User getCurrentUser() {
        if (currentUser == null) {
            throw new IllegalStateException("Current user is not set.");
        }
        return currentUser;
    }

    public static void backToProfile() {
        if (currentUser != null) {
            openUserScene(currentUser);
        }
    }

    /* ================= SCENES ================= */

    public static void openLoginScene() {
        switchTo("/views/login.fxml", "Cas nastal+", "/window/Login.css");
    }

    public static void openRegistrationWindow() {
        switchTo("/views/registration.fxml", "Cas nastal+", null);
    }

    public static void openCreateTerm() {
        switchTo("/views/createTerm.fxml", "Cas nastal+", null);
    }

    public static void openScheduleWindow() {
        switchTo("/views/schedule.fxml", "Cas nastal+", null);
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
            scene.getRoot().getProperties().put("windowCss", null);

            applyStyles(scene, null);

            activeStage.setScene(scene);
            activeStage.setTitle("Cas nastal+");
            activeStage.setResizable(false);
            activeStage.centerOnScreen();
            activeStage.show();

        } catch (Exception e) {
            throw new RuntimeException("Cannot load user scene", e);
        }
    }

    public static void openMessageWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    SceneManager.class.getResource("/views/message.fxml")
            );
            Parent root = loader.load();

            Scene scene = new Scene(root);
            scene.getRoot().getProperties().put("windowCss", null);

            applyStyles(scene, null);

            Stage stage = new Stage();
            stage.setTitle("Messages");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initOwner(activeStage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.showAndWait();

        } catch (Exception e) {
            throw new RuntimeException("Cannot open message window", e);
        }
    }

    /* ================= CORE ================= */

    private static void switchTo(String fxml, String title, String windowCss) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    SceneManager.class.getResource(fxml)
            );
            Parent root = loader.load();

            Scene scene = new Scene(root);
            scene.getRoot().getProperties().put("windowCss", windowCss);

            applyStyles(scene, windowCss);

            activeStage.setScene(scene);
            activeStage.setTitle(title);
            activeStage.setResizable(false);
            activeStage.centerOnScreen();
            activeStage.show();

        } catch (Exception e) {
            throw new RuntimeException("Cannot load FXML: " + fxml, e);
        }
    }

    private static void applyStyles(Scene scene, String windowCss) {
        scene.getStylesheets().clear();

        // theme
        URL themeUrl = SceneManager.class.getResource(themeCss());
        if (themeUrl == null) {
            throw new IllegalStateException("Theme CSS not found: " + themeCss());
        }
        scene.getStylesheets().add(themeUrl.toExternalForm());

        // window css (optional)
        if (windowCss != null) {
            URL windowUrl = SceneManager.class.getResource(windowCss);
            if (windowUrl == null) {
                throw new IllegalStateException("Window CSS not found: " + windowCss);
            }
            scene.getStylesheets().add(windowUrl.toExternalForm());
        }
    }


    public static void openEditTermWindow(Term term) {
        openCreateEditTermWindow(term);
    }

    private static void openCreateEditTermWindow(Term term) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    SceneManager.class.getResource("/views/createTerm.fxml")
            );
            Parent root = loader.load();

            CreateTermController controller = loader.getController();
            if (term != null) {
                controller.setTermToEdit(term);
            }

            Scene scene = new Scene(root);
            applyTheme(scene);

            Stage stage = new Stage();
            stage.setTitle(term == null ? "Create term" : "Edit term");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initOwner(primaryStage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.centerOnScreen();
            stage.showAndWait();

        } catch (Exception e) {
            throw new RuntimeException("Cannot open create/edit term window", e);
        }
    }

    private static void applyTheme(Scene scene) {
        scene.getStylesheets().clear();

        String css = darkTheme
                ? "/css/theme-dark.css"
                : "/css/theme-light.css";

        var url = SceneManager.class.getResource(css);
        if (url == null) {
            throw new IllegalStateException("CSS not found: " + css);
        }

        scene.getStylesheets().add(url.toExternalForm());
    }

}
