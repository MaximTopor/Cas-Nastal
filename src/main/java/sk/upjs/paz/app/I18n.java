package sk.upjs.paz.app;

import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public final class I18n {

    private static Locale locale = new Locale("sk");

    private I18n() {}

    public static Locale getLocale() {
        return locale;
    }

    public static void setLocale(Locale newLocale) {
        if (newLocale != null) locale = newLocale;
    }

    public static ResourceBundle bundle() {
        // базова назва БЕЗ .properties і БЕЗ _sk/_uk
        return ResourceBundle.getBundle("i18n.messages", locale);
    }

    public static FXMLLoader loader(String fxmlPath) throws IOException {
        URL url = I18n.class.getResource(fxmlPath);
        if (url == null) throw new IOException("FXML not found: " + fxmlPath);
        return new FXMLLoader(url, bundle());
    }
}
