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

    public enum Theme {
        LIGHT, DARK
    }

    private static Theme currentTheme = detectSystemTheme();

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

                            if (value.endsWith("0x0")) {
                                return Theme.DARK;
                            }

                            return Theme.LIGHT;
                        }
                    }
                }
            }


            if (os.contains("mac")) {
                Process process = new ProcessBuilder(
                        "defaults",
                        "read",
                        "-g",
                        "AppleInterfaceStyle"
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

    public static boolean isDarkTheme() {
        return currentTheme == Theme.DARK;
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

    public static void openLoginScene() {
        String css = isDarkTheme()
                ? "/css/login-dark.css"
                : "/css/login-light.css";

        switchTo("/views/login.fxml", "Cas nastal+", css);
    }

    public static void openRegistrationWindow() {
        String css = isDarkTheme()
                ? "/css/registration-dark.css"
                : "/css/registration-light.css";

        switchTo("/views/registration.fxml", "Cas nastal+", css);
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
            String messageCss = isDarkTheme()
                    ? "/css/message-dark.css"
                    : "/css/message-light.css";

            switchTo("/views/Message.fxml", "Cas nastal+", messageCss);
    }

    public static void openUserManagerWindow() {
        String css = isDarkTheme()
                ? "/css/user-manager-dark.css"
                : "/css/user-manager-light.css";

        switchTo("/views/UserManager.fxml", "Cas nastal+", css);
    }

    public static void openChangeDistrictWindow(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    SceneManager.class.getResource("/views/ChangeDistrict.fxml")
            );

            Parent root = loader.load();

            ChangeDistrictController controller = loader.getController();
            controller.setUser(user);

            Scene scene = new Scene(root);


            scene.getStylesheets().clear();

            if (isDarkTheme()) {
                scene.getStylesheets().add(
                        SceneManager.class
                                .getResource("/css/change-district-dark.css")
                                .toExternalForm()
                );
            } else {
                scene.getStylesheets().add(
                        SceneManager.class
                                .getResource("/css/change-district-light.css")
                                .toExternalForm()
                );
            }

            activeStage.setScene(scene);
            activeStage.setTitle("Cas nastal+");
            activeStage.centerOnScreen();
            activeStage.show();

        } catch (Exception e) {
            throw new RuntimeException("Cannot open ChangeDistrict view", e);
        }
    }

    public static void openCreateTerm() {
        String css = isDarkTheme()
                ? "/css/create-term-dark.css"
                : "/css/create-term-light.css";

        switchTo("/views/CreateTerm.fxml", "Cas nastal+", css);
    }

    public static void openScheduleWindow() {
        String css = isDarkTheme()
                ? "/css/schedule-dark.css"
                : "/css/schedule-light.css";

        switchTo("/views/schedule.fxml", "Cas nastal+", css);
    }


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

    public static void openCreateEditTermWindow(Term term) {
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

            Stage stage = new Stage();
            stage.setTitle(term == null ? "Cas nastal+" : "Cas nastal+");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initOwner(activeStage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.centerOnScreen();
            stage.showAndWait();

        } catch (IOException e) {
            throw new RuntimeException("Cannot open create/edit term window", e);
        }
    }

    public static void openEditDistrictWindow(District district) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    SceneManager.class.getResource("/views/EditDistrict.fxml")
            );

            Parent root = loader.load();

            EditDistrictController controller = loader.getController();
            controller.setDistrict(district);

            Scene scene = new Scene(root);


            scene.getStylesheets().clear();

            if (isDarkTheme()) {
                scene.getStylesheets().add(
                        SceneManager.class
                                .getResource("/css/edit-district-dark.css")
                                .toExternalForm()
                );
            } else {
                scene.getStylesheets().add(
                        SceneManager.class
                                .getResource("/css/edit-district-light.css")
                                .toExternalForm()
                );
            }


            activeStage.setScene(scene);
            activeStage.setTitle("Cas nastal+");
            activeStage.centerOnScreen();
            activeStage.show();

        } catch (Exception e) {
            throw new RuntimeException("Cannot open EditDistrict view", e);
        }
    }

    public static void openUserEditWindow(User user){
        try {
            FXMLLoader loader = new FXMLLoader(
                    SceneManager.class.getResource("/views/EditUser.fxml")
            );

            Parent root = loader.load();

            EditUserController controller = loader.getController();
            controller.setUser(user);

            Scene scene = new Scene(root);

            scene.getStylesheets().clear();

            if (isDarkTheme()) {
                scene.getStylesheets().add(
                        SceneManager.class
                                .getResource("/css/user-edit-dark.css")
                                .toExternalForm()
                );
            } else {
                scene.getStylesheets().add(
                        SceneManager.class
                                .getResource("/css/user-edit-light.css")
                                .toExternalForm()
                );
            }

            activeStage.setScene(scene);
            activeStage.setTitle("Cas nastal+");
            activeStage.setResizable(false);
            activeStage.centerOnScreen();
            activeStage.show();

        } catch (Exception e) {
            throw new RuntimeException("Cannot open EditUser view", e);
        }
    }

    public static void closeModal() {
        if (activeStage != null) {
            activeStage.close();
        }
    }

    public static void openChangeStatusWindow(User user, Status oldStatus, Status newStatus) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    SceneManager.class.getResource("/views/statusManagement.fxml")
            );

            Parent root = loader.load();

            StatusManagementController controller =
                    loader.getController();

            controller.init(
                    user,
                    oldStatus,
                    newStatus,
                    getCurrentUser().getIdUser()
            );

            Scene scene = new Scene(root);
            scene.getRoot().getProperties().put("windowCss", null);

            applyStyles(scene, null);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Change status");
            stage.initOwner(activeStage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.showAndWait();

        } catch (Exception e) {
            throw new RuntimeException("Cannot open ChangeStatus window", e);
        }
    }

}
