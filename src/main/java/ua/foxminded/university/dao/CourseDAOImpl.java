package ua.foxminded.university.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ua.foxminded.university.data.Course;
import ua.foxminded.university.domain.ConnectionManager;

public class CourseDAOImpl implements CourseDAO {
    private static final String SQL_INSERT_COURSE = "INSERT INTO university.COURSES (COURSE_NAME, COURSE_DESCRIPTION) VALUES (?, ?)";
    private static final String SQL_GET_ALL_COURSES = "SELECT ID, COURSE_NAME, COURSE_DESCRIPTION FROM university.COURSES";
    private static final String SQL_UPDATE_COURSE = "UPDATE university.COURSES SET COURSE_NAME=?, COURSE_DESCRIPTION=? WHERE ID=?";
    private static final String SQL_DELETE_COURSE = "DELETE FROM university.COURSES WHERE ID=?";
    private static final String SQL_GET_BY_ID = "SELECT ID, COURSE_NAME, COURSE_DESCRIPTION FROM university.COURSES WHERE ID=?";
    private static final String SQL_GET_BY_COURSE_NAME = "SELECT ID, COURSE_NAME, COURSE_DESCRIPTION FROM university.COURSES WHERE COURSE_NAME=?";
    private static final String ID = "ID";
    private static final String COURSE_NAME = "COURSE_NAME";
    private static final String COURSE_DESCRIPTION = "COURSE_DESCRIPTION";
    private static final String COURSE_ID = "COURSE_ID";
    private static final String SQL_GET_COURSE_BY_STUDENT_ID = "SELECT COURSE_ID FROM university.students_courses WHERE STUDENT_ID=?";
    private static final String ERROR_TEXT = "Check the argument. It might be empty or incorrrect ";
    private static final String SQL_DELETE_COURSE_FROM_STUDENT = "DELETE FROM university.students_courses WHERE STUDENT_ID=? and COURSE_ID=?";
    private static final String CHECK_QUERY = "Check the query or duplicate key value, or Database access ";
    private static final String NULL_ERROR_MESSAGE = "Argument cannot be null ";

    @Override
    public Course insert(Course course) throws DaoException {
        if (course == null) {
            throw new IllegalArgumentException(NULL_ERROR_MESSAGE);
        }

        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_INSERT_COURSE)) {
            statement.setString(1, course.getName());
            statement.setString(2, course.getDescription());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new DaoException(CHECK_QUERY, exception);
        }
        return course;
    }

    @Override
    public List<Course> getAll() throws DaoException {
        List<Course> listOfCourses = new ArrayList<>();

        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_GET_ALL_COURSES);
                ResultSet resultSet = statement.executeQuery();) {
            while (resultSet.next()) {
                int id = resultSet.getInt(ID);
                String name = resultSet.getString(COURSE_NAME);
                String description = resultSet.getString(COURSE_DESCRIPTION);
                Course course = new Course(id, name, description);
                listOfCourses.add(course);
            }
        } catch (SQLException exception) {
            throw new DaoException(CHECK_QUERY, exception);
        }
        return listOfCourses;
    }

    @Override
    public Course update(Course course) throws DaoException {
        if (course == null) {
            throw new IllegalArgumentException(NULL_ERROR_MESSAGE);
        }

        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_COURSE);) {
            statement.setString(1, course.getName());
            statement.setString(2, course.getDescription());
            statement.setInt(3, course.getId());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new DaoException(CHECK_QUERY, exception);
        }
        return course;
    }

    @Override
    public Course remove(Course course) throws DaoException {
        if (course == null) {
            throw new IllegalArgumentException(NULL_ERROR_MESSAGE);
        }

        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_DELETE_COURSE);) {
            statement.setInt(1, course.getId());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new DaoException(CHECK_QUERY, exception);
        }
        return course;
    }

    @Override
    public Course getById(int id) throws DaoException {
        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_GET_BY_ID);) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery();) {
                if (resultSet.next()) {
                    int courseId = resultSet.getInt(ID);
                    String courseName = resultSet.getString(COURSE_NAME);
                    String courseDescription = resultSet.getString(COURSE_DESCRIPTION);
                    return new Course(courseId, courseName, courseDescription);
                } else {
                    throw new DaoException(ERROR_TEXT);
                }
            }
        } catch (SQLException exception) {
            throw new DaoException(CHECK_QUERY, exception);
        }
    }

    @Override
    public void removeStudentFromCourse(int studentId, int courseId) throws DaoException {
        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_DELETE_COURSE_FROM_STUDENT);) {
            statement.setInt(1, studentId);
            statement.setInt(2, courseId);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new DaoException(CHECK_QUERY, exception);
        }
    }

    @Override
    public List<Course> findCoursesByStudentId(int studentID) throws DaoException {
        List<Course> listOfCourses = new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_GET_COURSE_BY_STUDENT_ID);) {
            statement.setInt(1, studentID);

            try (ResultSet resultSet = statement.executeQuery();) {
                while (resultSet.next()) {
                    int courseId = resultSet.getInt(COURSE_ID);
                    Course course = getById(courseId);
                    listOfCourses.add(course);
                }
            }
        } catch (SQLException exception) {
            throw new DaoException(CHECK_QUERY, exception);
        }
        return listOfCourses;
    }

    @Override
    public Course findByName(String name) throws DaoException {
        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_GET_BY_COURSE_NAME);) {
            statement.setString(1, name);

            try (ResultSet resultSet = statement.executeQuery();) {
                if (resultSet.next()) {
                    int courseId = resultSet.getInt(ID);
                    String courseName = resultSet.getString(COURSE_NAME);
                    String courseDescription = resultSet.getString(COURSE_DESCRIPTION);
                    return new Course(courseId, courseName, courseDescription);
                } else {
                    throw new DaoException(ERROR_TEXT);
                }
            }
        } catch (SQLException exception) {
            throw new DaoException(CHECK_QUERY, exception);
        }
    }
}
