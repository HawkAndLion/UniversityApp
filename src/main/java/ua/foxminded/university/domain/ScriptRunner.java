package ua.foxminded.university.domain;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class ScriptRunner {
    private static final String ERROR_MESSAGE = "File should not be empty ";

    public void runSQLScript(String sqlFile, Connection connection) throws SQLException {
        if (sqlFile.isEmpty()) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }

        Scanner scanner = new Scanner(ScriptRunner.class.getClassLoader().getResourceAsStream(sqlFile));
        try (Statement statement = connection.createStatement();) {
            while (scanner.hasNextLine()) {
                statement.execute(scanner.nextLine());
            }
        }
    }
}
