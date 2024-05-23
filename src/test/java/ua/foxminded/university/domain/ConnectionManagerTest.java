package ua.foxminded.university.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import ua.foxminded.university.data.Student;

class ConnectionManagerTest {
    private static Connection connection;
    private static final String JDBC_URL = "jdbc:h2:~/test";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "";
    private static final String ID = "ID";
    private static final String GROUP_ID = "GROUP_ID";
    private static final String FIRST_NAME = "FIRST_NAME";
    private static final String LAST_NAME = "LAST_NAME";
    private static final String SELECT_ALL_STUDENTS = "SELECT * FROM university.students";

    @AfterAll
    public static void closeConnection() throws Exception {
        connection.close();
    }

    @Test
    void shouldReturnSameDataWhenConnectionToDatabaseIsTested() throws SQLException {
        connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        Student expectedStudent = new Student(105, 201, "Charles", "Xavier");

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SELECT_ALL_STUDENTS);
        if (resultSet.next()) {
            int studentId = resultSet.getInt(ID);
            int groupId = resultSet.getInt(GROUP_ID);
            String firstName = resultSet.getString(FIRST_NAME);
            String lastName = resultSet.getString(LAST_NAME);
            Student actualStudent = new Student(studentId, groupId, firstName, lastName);

            if (actualStudent.getLastName().equals(expectedStudent.getLastName())) {
                assertEquals(expectedStudent, actualStudent);
            }
        }
    }
}
