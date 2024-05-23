package ua.foxminded.university.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ua.foxminded.university.data.Course;
import ua.foxminded.university.data.Student;
import ua.foxminded.university.domain.ConnectionManager;

public class StudentDAOImpl implements StudentDAO {
    private static final String SQL_GET_BY_STUDENT_ID = "SELECT ID, GROUP_ID, FIRST_NAME, LAST_NAME FROM university.STUDENTS WHERE ID=?";
    private static final String SQL_GET_ALL_STUDENTS = "SELECT ID, GROUP_ID, FIRST_NAME, LAST_NAME FROM university.STUDENTS";
    private static final String SQL_GET_BY_LAST_NAME = "SELECT ID, GROUP_ID, FIRST_NAME, LAST_NAME FROM university.STUDENTS WHERE LAST_NAME=?";
    private static final String SQL_ADD_STUDENT = "INSERT INTO university.STUDENTS (GROUP_ID, FIRST_NAME, LAST_NAME) VALUES (?, ?, ?)";
    private static final String SQL_UPDATE_STUDENT = "UPDATE university.STUDENTS SET GROUP_ID=?, FIRST_NAME=?, LAST_NAME=? WHERE ID=?";
    private static final String SQL_DELETE_STUDENT = "DELETE FROM university.STUDENTS WHERE ID=?";
    private static final String ID = "ID";
    private static final String GROUP_ID = "GROUP_ID";
    private static final String FIRST_NAME = "FIRST_NAME";
    private static final String LAST_NAME = "LAST_NAME";
    private static final String SQL_ADD_COURSE_TO_STUDENT = "INSERT INTO university.students_courses (STUDENT_ID, COURSE_ID) VALUES (?, ?)";
    private static final String SQL_DELETE_STUDENT_FROM_COURSE = "DELETE FROM university.students_courses WHERE STUDENT_ID=?";
    private static final String STUDENT_ID = "STUDENT_ID";
    private static final String SQL_GET_STUDENT_BY_COURSE_ID = "SELECT STUDENT_ID FROM university.students_courses WHERE COURSE_ID=?";
    private static final String CHECK_QUERY = "Check the query or duplicate key value, or Database access ";
    private static final String NULL_ERROR_MESSAGE = "Argument cannot be null ";
    private static final String ERROR_TEXT = "Check the argument. It might be empty ";

    @Override
    public Student insert(Student student) throws DaoException {
        if (student == null) {
            throw new IllegalArgumentException(NULL_ERROR_MESSAGE);
        }

        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_ADD_STUDENT);) {
            statement.setInt(1, student.getGroupId());
            statement.setString(2, student.getFirstName());
            statement.setString(3, student.getLastName());

            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new DaoException(CHECK_QUERY, exception);
        }
        return student;
    }

    @Override
    public List<Student> getAll() throws DaoException {
        List<Student> listOfStudents = new ArrayList<>();

        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_GET_ALL_STUDENTS);
                ResultSet resultSet = statement.executeQuery();) {

            while (resultSet.next()) {
                int studentId = resultSet.getInt(ID);
                int groupId = resultSet.getInt(GROUP_ID);
                String firstName = resultSet.getString(FIRST_NAME);
                String lastName = resultSet.getString(LAST_NAME);

                Student student = new Student(studentId, groupId, firstName, lastName);

                listOfStudents.add(student);
            }
        } catch (SQLException exception) {
            throw new DaoException(CHECK_QUERY, exception);
        }
        return listOfStudents;
    }

    @Override
    public Student update(Student student) throws DaoException {
        if (student == null) {
            throw new IllegalArgumentException(NULL_ERROR_MESSAGE);
        }

        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_STUDENT);) {

            statement.setInt(1, student.getGroupId());
            statement.setString(2, student.getFirstName());
            statement.setString(3, student.getLastName());
            statement.setInt(4, student.getStudentId());

            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new DaoException(CHECK_QUERY, exception);
        }
        return student;
    }

    @Override
    public Student remove(Student student) throws DaoException {
        if (student == null) {
            throw new IllegalArgumentException(NULL_ERROR_MESSAGE);
        }

        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_DELETE_STUDENT);) {

            statement.setInt(1, student.getStudentId());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new DaoException(CHECK_QUERY, exception);
        }
        return student;
    }

    @Override
    public Student getById(int studentId) throws DaoException {
        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_GET_BY_STUDENT_ID);) {

            statement.setInt(1, studentId);

            try (ResultSet resultSet = statement.executeQuery();) {
                if (resultSet.next()) {
                    int studentID = resultSet.getInt(ID);
                    String firstName = resultSet.getString(FIRST_NAME);
                    String lastName = resultSet.getString(LAST_NAME);
                    int groupId = resultSet.getInt(GROUP_ID);

                    return new Student(studentID, groupId, firstName, lastName);
                } else {
                    throw new DaoException(ERROR_TEXT);
                }
            }
        } catch (SQLException exception) {
            throw new DaoException(CHECK_QUERY, exception);
        }
    }

    @Override
    public void insertCourse(Student student, Course course) throws DaoException {
        if (student == null || course == null) {
            throw new IllegalArgumentException(NULL_ERROR_MESSAGE);
        }

        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_ADD_COURSE_TO_STUDENT);) {
            statement.setInt(1, student.getStudentId());
            statement.setInt(2, course.getId());

            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new DaoException(CHECK_QUERY, exception);
        }
    }

    @Override
    public Student removeStudentFromCourse(Student student, Course course) throws DaoException {
        if (student == null || course == null) {
            throw new IllegalArgumentException(NULL_ERROR_MESSAGE);
        }

        try (Connection connection = ConnectionManager.getConnection();

                PreparedStatement statement = connection.prepareStatement(SQL_DELETE_STUDENT_FROM_COURSE);) {

            statement.setInt(1, student.getStudentId());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new DaoException(CHECK_QUERY, exception);
        }
        return student;
    }

    @Override
    public List<Student> getStudentListByCourseName(String courseName) throws DaoException {
        CourseDAO courseDAO = new CourseDAOImpl();
        int courseID = courseDAO.findByName(courseName).getId();
        List<Student> list = new ArrayList<>();

        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_GET_STUDENT_BY_COURSE_ID);) {

            statement.setInt(1, courseID);
            try (ResultSet resultSet = statement.executeQuery();) {
                while (resultSet.next()) {
                    int studentId = resultSet.getInt(STUDENT_ID);

                    Student student = getById(studentId);
                    list.add(student);
                }
            }
        } catch (SQLException exception) {
            throw new DaoException(CHECK_QUERY, exception);
        }
        return list;
    }

    @Override
    public Student findByLastName(String name) throws DaoException {
        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_GET_BY_LAST_NAME);) {
            statement.setString(1, capitalizeName(name));

            try (ResultSet resultSet = statement.executeQuery();) {
                if (resultSet.next()) {
                    int studentId = resultSet.getInt(ID);
                    int groupId = resultSet.getInt(GROUP_ID);
                    String firstName = resultSet.getString(FIRST_NAME);
                    String lastName = resultSet.getString(LAST_NAME);

                    return new Student(studentId, groupId, firstName, lastName);
                } else {
                    throw new DaoException(ERROR_TEXT);
                }
            }
        } catch (SQLException exception) {
            throw new DaoException(CHECK_QUERY, exception);
        }
    }

    private String capitalizeName(String name) {
        String lastName = name.toLowerCase();
        return lastName.substring(0, 1).toUpperCase() + lastName.substring(1);
    }
}
