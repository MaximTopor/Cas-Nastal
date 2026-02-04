package sk.upjs.paz.app;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sk.upjs.paz.controller.*;
import sk.upjs.paz.model.District;
import sk.upjs.paz.model.Status;
import sk.upjs.paz.model.Term;
import sk.upjs.paz.model.User;

import java.io.IOException;
import java.net.URL;

public class SceneManager {

    private static User currentUser;

    public enum Theme { LIGHT, DARK }

    private static Theme currentTheme = detectSystemTheme();

    private static Stage activeStage; // ONLY main window stage

    public static void setStage(Stage stage) {
        activeStage = stage;
    }

    public static User getCurrentUser() {
        if (currentUser == null) {
            throw new IllegalStateException("Current user is not set.");
        }
        return currentUser;
    }

    // -------------------- Theme --------------------

    public static boolean isDarkTheme() {
        return currentTheme == Theme.DARK;
    }

    private static String themeCss() {
        return currentTheme == Theme.DARK
                ? "/css/user-dark.css"
                : "/css/user-light.css";
    }

    private static Theme detectSystemTheme() {
        String os = System.getProperty("os.name").toLowerCase();

        try {
            if (os.contains("win")) {
                Process process = new ProcessBuilder(
                        "reg",
                        "query",
                        "HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize",
                        "/v",
                        "AppsUseLightTheme"
                ).start();

                try (var reader = new java.io.BufferedReader(
                        new java.io.InputStreamReader(process.getInputStream())
                )) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.contains("AppsUseLightTheme")) {
                            String value = line.trim();
                            if (value.endsWith("0x0")) return Theme.DARK;
                            return Theme.LIGHT;
                        }
                    }
                }
            }

            if (os.contains("mac")) {
                Process process = new ProcessBuilder(
                        "defaults", "read", "-g", "AppleInterfaceStyle"
                ).start();

                try (var reader = new java.io.BufferedReader(
                        new java.io.InputStreamReader(process.getInputStream())
                )) {
                    String result = reader.readLine();
                    if (result != null && result.equalsIgnoreCase("Dark")) {
                        return Theme.DARK;
                    }
                }
            }
        } catch (Exception ignored) {
        }

        return Theme.LIGHT;
    }

    public static void toggleTheme(Scene scene) {
        currentTheme = (currentTheme == Theme.DARK) ? Theme.LIGHT : Theme.DARK;

        if (activeStage != null && activeStage.getScene() != null) {
            String windowCss = (String) activeStage.getScene().getRoot()
                    .getProperties().get("windowCss");
            applyStyles(activeStage.getScene(), windowCss);
        }
    }

    private static void applyStyles(Scene scene, String windowCss) {
        scene.getStylesheets().clear();

        URL themeUrl = SceneManager.class.getResource(themeCss());
        if (themeUrl == null) {
            throw new IllegalStateException("Theme CSS not found: " + themeCss());
        }
        scene.getStylesheets().add(themeUrl.toExternalForm());

        if (windowCss != null) {
            URL windowUrl = SceneManager.class.getResource(windowCss);
            if (windowUrl == null) {
                throw new IllegalStateException("Window CSS not found: " + windowCss);
            }
            scene.getStylesheets().add(windowUrl.toExternalForm());
        }
    }

    // -------------------- Main windows (replace scene in activeStage) --------------------

    public static void openLoginScene() {
        String css = isDarkTheme() ? "/css/login-dark.css" : "/css/login-light.css";
        switchToMain("/views/login.fxml", "Cas nastal+", css);
    }

    public static void openRegistrationWindow() {
        String css = isDarkTheme() ? "/css/registration-dark.css" : "/css/registration-light.css";
        switchToMain("/views/registration.fxml", "Cas nastal+", css);
    }

    public static void openMessageWindow() {
        String css = isDarkTheme() ? "/css/message-dark.css" : "/css/message-light.css";
        switchToMain("/views/Message.fxml", "Cas nastal+", css);
    }

    public static void openUserManagerWindow() {
        String css = isDarkTheme() ? "/css/user-manager-dark.css" : "/css/user-manager-light.css";
        switchToMain("/views/UserManager.fxml", "Cas nastal+", css);
    }

    public static void openScheduleWindow() {
        String css = isDarkTheme() ? "/css/schedule-dark.css" : "/css/schedule-light.css";
        switchToMain("/views/schedule.fxml", "Cas nastal+", css);
    }

    public static void openUserScene(User user) {
        currentUser = user;

        try {
            FXMLLoader loader = I18n.loader("/views/user.fxml");
            Parent root = loader.load();

            UserController controller = loader.getController();
            controller.setUser(user);

            Scene scene = new Scene(root);
            scene.getRoot().getProperties().put("windowCss", null);
            applyStyles(scene, null);

            ensureMainStage();
            activeStage.setScene(scene);
            activeStage.setTitle("Cas nastal+");
            activeStage.setResizable(true);
            activeStage.centerOnScreen();
            activeStage.show();
        } catch (Exception e) {
            throw new RuntimeException("Cannot load user scene", e);
        }
    }

    private static void switchToMain(String fxml, String title, String windowCss) {
        try {
            FXMLLoader loader = I18n.loader(fxml);

            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getRoot().getProperties().put("windowCss", windowCss);

            applyStyles(scene, windowCss);

            ensureMainStage();
            activeStage.setScene(scene);
            activeStage.setTitle(title);
            activeStage.setResizable(true);
            activeStage.centerOnScreen();
            activeStage.show();
        } catch (Exception e) {
            throw new RuntimeException("Cannot load FXML: " + fxml, e);
        }
    }

    private static void ensureMainStage() {
        if (activeStage == null) {
            throw new IllegalStateException("Main stage is not set. Call SceneManager.setStage(primaryStage).");
        }
    }

    // -------------------- Modal windows (separate Stage) --------------------

    /**
     * Opens a modal window and returns the Stage. Caller may close it if needed.
     */
    private static Stage openModal(String fxml, String title, String css) {
        try {
            FXMLLoader loader = I18n.loader(fxml);
            Parent root = loader.load();

            Scene scene = new Scene(root);
            scene.getStylesheets().clear();

            if (css != null) {
                URL cssUrl = SceneManager.class.getResource(css);
                if (cssUrl == null) throw new IllegalStateException("CSS not found: " + css);
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(scene);
            stage.initOwner(activeStage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(true);
            stage.centerOnScreen();
            return stage;

        } catch (Exception e) {
            throw new RuntimeException("Cannot open modal: " + fxml, e);
        }
    }

    public static void closeModal(Stage modalStage) {
        if (modalStage != null) {
            modalStage.close();
        }
    }

    public static void openCreateEditTermWindow(Term term) {
        try {
            FXMLLoader loader = I18n.loader("/views/createTerm.fxml");
            Parent root = loader.load();

            CreateTermController controller = loader.getController();
            if (term != null) {
                controller.setTermToEdit(term);
            }

            Scene scene = new Scene(root);

            Stage stage = new Stage();
            stage.setTitle("Cas nastal+");
            stage.setScene(scene);
            stage.setResizable(true);
            stage.initOwner(activeStage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.centerOnScreen();
            stage.showAndWait();

        } catch (IOException e) {
            throw new RuntimeException("Cannot open create/edit term window", e);
        }
    }

    public static void openChangeStatusWindow(User user, Status oldStatus, Status newStatus) {
        String css = isDarkTheme() ? "/css/status-dark.css" : "/css/status-light.css";

        try {
            FXMLLoader loader = I18n.loader("/views/statusManagement.fxml");
            Parent root = loader.load();

            StatusManagementController controller = loader.getController();
            controller.init(user, oldStatus, newStatus, getCurrentUser().getIdUser());

            Scene scene = new Scene(root);
            scene.getStylesheets().clear();

            URL cssUrl = SceneManager.class.getResource(css);
            if (cssUrl == null) throw new IllegalStateException("CSS not found: " + css);
            scene.getStylesheets().add(cssUrl.toExternalForm());

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Change status");
            stage.initOwner(activeStage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(true);
            stage.centerOnScreen();
            stage.showAndWait();

        } catch (Exception e) {
            throw new RuntimeException("Cannot open ChangeStatus window", e);
        }
    }

    public static void openChangeDistrictWindow(User user) {
        String css = isDarkTheme() ? "/css/change-district-dark.css" : "/css/change-district-light.css";

        try {
            FXMLLoader loader = I18n.loader("/views/ChangeDistrict.fxml");
            Parent root = loader.load();

            ChangeDistrictController controller = loader.getController();
            controller.setUser(user);

            Scene scene = new Scene(root);
            scene.getStylesheets().clear();

            URL cssUrl = SceneManager.class.getResource(css);
            if (cssUrl == null) throw new IllegalStateException("CSS not found: " + css);
            scene.getStylesheets().add(cssUrl.toExternalForm());

            ensureMainStage();
            activeStage.setScene(scene);
            activeStage.setTitle("Cas nastal+");
            activeStage.centerOnScreen();
            activeStage.show();

        } catch (Exception e) {
            throw new RuntimeException("Cannot open ChangeDistrict view", e);
        }
    }

    public static void openEditDistrictWindow(District district) {
        String css = isDarkTheme() ? "/css/edit-district-dark.css" : "/css/edit-district-light.css";

        try {
            FXMLLoader loader = I18n.loader("/views/EditDistrict.fxml");
            Parent root = loader.load();

            EditDistrictController controller = loader.getController();
            controller.setDistrict(district);

            Scene scene = new Scene(root);
            scene.getStylesheets().clear();

            URL cssUrl = SceneManager.class.getResource(css);
            if (cssUrl == null) throw new IllegalStateException("CSS not found: " + css);
            scene.getStylesheets().add(cssUrl.toExternalForm());

            ensureMainStage();
            activeStage.setScene(scene);
            activeStage.setTitle("Cas nastal+");
            activeStage.centerOnScreen();
            activeStage.show();

        } catch (Exception e) {
            throw new RuntimeException("Cannot open EditDistrict view", e);
        }
    }

    public static void openUserEditWindow(User user) {
        String css = isDarkTheme() ? "/css/user-edit-dark.css" : "/css/user-edit-light.css";

        try {
            FXMLLoader loader = I18n.loader("/views/EditUser.fxml");
            Parent root = loader.load();

            EditUserController controller = loader.getController();
            controller.setUser(user);

            Scene scene = new Scene(root);
            scene.getStylesheets().clear();

            URL cssUrl = SceneManager.class.getResource(css);
            if (cssUrl == null) throw new IllegalStateException("CSS not found: " + css);
            scene.getStylesheets().add(cssUrl.toExternalForm());

            ensureMainStage();
            activeStage.setScene(scene);
            activeStage.setTitle("Cas nastal+");
            activeStage.centerOnScreen();
            activeStage.show();

        } catch (Exception e) {
            throw new RuntimeException("Cannot open EditUser view", e);
        }
    }
}
