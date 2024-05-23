package ua.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ua.foxminded.university.data.Course;
import ua.foxminded.university.data.Student;

class CourseDAOImplTest {
    private static final int COURSE_ID = 2;
    private static final String ALGEBRA = "Algebra";
    private static final String ALGEBRA_DESCRIPTION = "Develops algebraic concepts and skills.";
    private static final String GEOMETRY = "Geometry";
    private static final String GEOMETRY_DESCRIPTION = "Presents the study of several geometries";
    private static final String BIOLOGY = "Biology";
    private static final String BIOLOGY_DESCRIPTION = "Exploring the great outdoors to the intricacies of cells and molecules.";
    private static final String GEOGRAPHY = "Geography";
    private static final String GEOGRAPHY_DESCRIPTION = "An introduction to the earth’s natural environmental systems.";
    private static final String MATH = "Math";
    private static final String MATH_DESCRIPTION = "Algebra + Geometry";
    private static final int NUMBER_OF_COURSES_INITIALLY = 3;
    private static final int NUMBER_OF_COURSES_AFTER_INSERTION = 4;
    private static final String NULL_ERROR_MESSAGE = "Argument cannot be null ";

    private CourseDAO courseDAO;
    private StudentDAO studentDAO;

    @BeforeEach
    void setUp() {
        courseDAO = new CourseDAOImpl();
        studentDAO = new StudentDAOImpl();
    }

    @Test
    void shouldReturnSameDataWhenNewObjectInserted() throws DaoException {
        Course expectedCourse = new Course(BIOLOGY, BIOLOGY_DESCRIPTION);
        Course actualCourse = courseDAO.insert(expectedCourse);
        assertEquals(expectedCourse, actualCourse);
    }

    @Test
    void shouldReturnSameSizeWhenListRequested() throws DaoException {
        List<Course> courseList = new ArrayList<>();
        courseList.add(new Course(1, ALGEBRA, ALGEBRA_DESCRIPTION));
        courseList.add(new Course(2, GEOMETRY, GEOMETRY_DESCRIPTION));
        courseList.add(new Course(3, GEOGRAPHY, GEOGRAPHY_DESCRIPTION));
        courseList.add(courseDAO.findByName(BIOLOGY));

        assertEquals(courseList.size(), courseDAO.getAll().size());
    }

    @Test
    void shouldReturnSameCourseWhenCourseUpdated() throws DaoException {
        Course expectedCourse = new Course(1, MATH, MATH_DESCRIPTION);
        Course actualCourse = courseDAO.update(expectedCourse);
        assertNotNull(actualCourse);
        assertEquals(expectedCourse, actualCourse);

        Course returnCourse = new Course(1, ALGEBRA, ALGEBRA_DESCRIPTION);
        courseDAO.update(returnCourse);
    }

    @Test
    void shouldReturnSameDataWhenRemoved() throws DaoException {
        Course course = new Course(MATH, MATH_DESCRIPTION);
        Course insertedCourse = courseDAO.insert(course);
        assertNotNull(insertedCourse);
        Course courseToRemove = courseDAO.findByName(MATH);
        courseDAO.remove(courseToRemove);

        List<Course> actual = new ArrayList<>();
        actual = courseDAO.getAll();
        if (actual.size() == NUMBER_OF_COURSES_INITIALLY) {
            assertEquals(NUMBER_OF_COURSES_INITIALLY, actual.size());
        } else {
            assertEquals(NUMBER_OF_COURSES_AFTER_INSERTION, actual.size());
        }
    }

    @Test
    void shouldReturnSameCourseWhenMethodExecuted() throws DaoException {
        Course expectedCourse = new Course(2, GEOMETRY, GEOMETRY_DESCRIPTION);
        Course actualCourse = courseDAO.getById(COURSE_ID);
        assertNotNull(actualCourse);
        assertEquals(expectedCourse, actualCourse);
    }

    @Test
    void shouldReturnSameDataWhenStudentRemovedFromCourse() throws DaoException {
        Student newStudent = new Student(207, "Katlin", "Fallon");
        Student insertedStudent = studentDAO.insert(newStudent);
        assertNotNull(insertedStudent);
        Student student = studentDAO.findByLastName("Fallon");

        studentDAO.insertCourse(student, courseDAO.findByName(GEOMETRY));

        List<Course> expectedCourseList = new ArrayList<>();
        expectedCourseList = courseDAO.getAll();
        List<Student> expectedStudentList = new ArrayList<>();
        expectedStudentList = studentDAO.getAll();
        int studentId = studentDAO.findByLastName("Fallon").getStudentId();
        int courseId = courseDAO.findByName(GEOMETRY).getId();

        courseDAO.removeStudentFromCourse(studentId, courseId);

        List<Course> actualCourseList = new ArrayList<>();
        actualCourseList = courseDAO.getAll();
        List<Student> actualStudentList = new ArrayList<>();
        actualStudentList = studentDAO.getAll();

        assertFalse(expectedCourseList.size() == actualCourseList.size() && expectedCourseList == actualCourseList
                && expectedCourseList.containsAll(actualCourseList));
        assertFalse(expectedStudentList.size() == actualStudentList.size() && expectedStudentList == actualStudentList
                && expectedStudentList.containsAll(actualStudentList));
    }

    @Test
    void shouldReturnSameListWhenStudentIdSpecified() throws DaoException {
        List<Course> expectedList = new ArrayList<>();
        expectedList.add(new Course(1, ALGEBRA, ALGEBRA_DESCRIPTION));
        expectedList.add(new Course(2, GEOMETRY, GEOMETRY_DESCRIPTION));
        Student newStudent = new Student(203, "Sandra", "Bullock");
        Student insertedStudent = studentDAO.insert(newStudent);
        assertNotNull(insertedStudent);
        Student student = studentDAO.findByLastName("Bullock");
        studentDAO.insertCourse(student, courseDAO.findByName(ALGEBRA));
        studentDAO.insertCourse(student, courseDAO.findByName(GEOMETRY));

        List<Course> actualList = new ArrayList<>();
        actualList = courseDAO.findCoursesByStudentId(student.getStudentId());

        assertEquals(expectedList, actualList);

    }

    @Test
    void shouldReturnSameCourseWhenMethodIsExecuted() throws DaoException {
        Course expected = new Course(1, ALGEBRA, ALGEBRA_DESCRIPTION);
        Course actual = courseDAO.findByName(ALGEBRA);
        assertEquals(expected, actual);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenNullInserted() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            courseDAO.insert(null);
        });

        assertEquals(NULL_ERROR_MESSAGE, exception.getMessage());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenNullPassedToUpdate() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            courseDAO.update(null);
        });

        assertEquals(NULL_ERROR_MESSAGE, exception.getMessage());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenNullPassedToRemove() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            courseDAO.remove(null);
        });

        assertEquals(NULL_ERROR_MESSAGE, exception.getMessage());
    }
}
