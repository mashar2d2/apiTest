package ru.masha;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

public class ApplicationProperty {

    private static final Properties PROPS = new Properties();

    static {
        try (InputStream input = ApplicationProperty.class.getClassLoader().getResourceAsStream("application.properties")) {
            PROPS.load(input);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public static String getBaseUrl() {
        return PROPS.getProperty("baseUrl");
    }
}
