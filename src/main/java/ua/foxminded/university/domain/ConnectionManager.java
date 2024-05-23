package ua.foxminded.university.domain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import lombok.NoArgsConstructor;
import ua.foxminded.university.reader.Reader;

@NoArgsConstructor
public class ConnectionManager {
    private static final String CONFIG_FILE_PATH = "config.properties";
    private static final String DB_HOST = "db.host";
    private static final String DB_LOGIN = "db.login";
    private static final String DB_PASSWORD = "db.password";

    public static Connection getConnection() throws SQLException {
        Reader reader = new Reader();
        Properties property;
        property = reader.getPropertiesFromConfigFile(CONFIG_FILE_PATH);

        String host = property.getProperty(DB_HOST);
        String login = property.getProperty(DB_LOGIN);
        String password = property.getProperty(DB_PASSWORD);

        return DriverManager.getConnection(host, login, password);
    }
}
