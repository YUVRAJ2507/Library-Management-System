package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL_VARIABLE = "LIBRARY_DB_URL";
    private static final String USER_VARIABLE = "LIBRARY_DB_USER";
    private static final String PASSWORD_VARIABLE = "LIBRARY_DB_PASSWORD";

    private DatabaseConnection() {
        // Prevent unnecessary object creation.
    }

    public static Connection getConnection() throws SQLException {

        String url = getRequiredEnvironmentVariable(URL_VARIABLE);
        String user = getRequiredEnvironmentVariable(USER_VARIABLE);
        String password = getRequiredEnvironmentVariable(PASSWORD_VARIABLE);

        return DriverManager.getConnection(url, user, password);
    }

    private static String getRequiredEnvironmentVariable(String name)
            throws SQLException {

        String value = System.getenv(name);

        if (value == null || value.isBlank()) {
            throw new SQLException("Missing required environment variable: " + name);
        }

        return value;
    }
}
