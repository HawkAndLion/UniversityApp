package ua.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ua.foxminded.university.data.Course;
import ua.foxminded.university.data.Student;

class StudentDAOImplTest {
    private static final String ALGEBRA = "Algebra";
    private static final String ALGEBRA_DESCRIPTION = "Develops algebraic concepts and skills.";
    private static final String ERROR_MESSAGE = "Argument cannot be null ";
    private StudentDAO studentDAO;
    private CourseDAO courseDAO;

    @BeforeEach
    void setUp() {
        studentDAO = new StudentDAOImpl();
        courseDAO = new CourseDAOImpl();
    }

    @Test
    void shouldReturnSameDataWhenInserted() throws DaoException {
        Student expectedStudent = new Student(203, "Oleg", "Kim");
        studentDAO.insert(expectedStudent);
        assertNotNull(expectedStudent);
        assertEquals(2, studentDAO.getAll().size());

        Student actualStudent = studentDAO.getById(studentDAO.findByLastName("Kim").getStudentId());
        assertNotNull(actualStudent);

        assertEquals(expectedStudent.getLastName(), actualStudent.getLastName());
        assertEquals(expectedStudent.getFirstName(), actualStudent.getFirstName());
    }

    @Test
    void shouldReturnSameDataWhenListRequested() throws DaoException {
        List<Student> expected = new ArrayList<>();
        List<Student> actual = new ArrayList<>();
        actual = studentDAO.getAll();
        assertNotNull(actual);

        if (actual.size() == 2) {
            expected.add(studentDAO.findByLastName("Kim"));
            expected.add(new Student(105, 201, "Charles", "Xavier"));
        } else {
            expected.add(new Student(105, 201, "Charles", "Xavier"));
        }

        assertEquals(expected, actual);
        assertEquals(expected.size(), actual.size());
    }

    @Test
    void shouldReturnSameDataWhenUpdated() throws DaoException {
        Student expectedStudent = new Student(105, 777, "John", "Xavier");
        Student actualStudent = studentDAO.update(expectedStudent);
        assertNotNull(actualStudent);
        assertEquals(expectedStudent, actualStudent);

        Student returnStudent = new Student(105, 201, "Charles", "Xavier");
        studentDAO.update(returnStudent);
    }

    @Test
    void shouldReturnSameDataStudentWhenDeleted() throws DaoException {
        Student newStudent = new Student(555, "Wendy", "Wong");
        Student insertedStudent = studentDAO.insert(newStudent);
        assertNotNull(insertedStudent);
        Student student = studentDAO.findByLastName("Wong");
        studentDAO.remove(student);

        List<Student> studentList = new ArrayList<>();
        studentList = studentDAO.getAll();

        assertFalse(studentList.contains(student));
    }

    @Test
    void shouldReturnSameStudentWhenGettingStudentById() throws DaoException {
        Student expectedStudent = new Student(105, 201, "Charles", "Xavier");
        Student actualStudent = studentDAO.getById(expectedStudent.getStudentId());
        assertNotNull(actualStudent);
        assertEquals(expectedStudent, actualStudent);
    }

    @Test
    void shouldReturnSameDataWhenCourseAdded() throws DaoException {
        Student student = studentDAO.findByLastName("Xavier");
        studentDAO.insertCourse(student, courseDAO.findByName("Algebra"));
        List<Course> listOfCourses = new ArrayList<>();
        listOfCourses = courseDAO.findCoursesByStudentId(student.getStudentId());
        List<Course> courseList = new ArrayList<>();
        for (Course course : listOfCourses) {
            courseList.add(course);
        }
        assertNotNull(courseList);
        Course expectedCourse = new Course(1, "Algebra", "Develops algebraic concepts and skills.");
        assertEquals(expectedCourse, courseList.get(0));
    }

    @Test
    void shouldReturnSameDataWhenStudentRemoved() throws DaoException {
        Student newStudent = new Student(333, "Alfred", "Shaw");
        studentDAO.insert(newStudent);
        Student student = studentDAO.findByLastName("Shaw");
        assertNotNull(student);
        Course course = courseDAO.findByName("Geometry");
        assertNotNull(course);
        List<Course> expectedCourseList = new ArrayList<>();
        expectedCourseList = courseDAO.getAll();
        List<Student> expectedStudentList = new ArrayList<>();
        expectedStudentList = studentDAO.getAll();

        studentDAO.insertCourse(student, course);
        studentDAO.removeStudentFromCourse(student, course);

        List<Course> actualCourseList = new ArrayList<>();
        actualCourseList = courseDAO.getAll();
        List<Student> actualStudentList = new ArrayList<>();
        actualStudentList = studentDAO.getAll();

        assertFalse(expectedCourseList.size() == actualCourseList.size() && expectedCourseList == actualCourseList
                && expectedCourseList.containsAll(actualCourseList));
        assertFalse(expectedStudentList.size() == actualStudentList.size() && expectedStudentList == actualStudentList
                && expectedStudentList.containsAll(actualStudentList));
        assertTrue(courseDAO.findCoursesByStudentId(student.getStudentId()).isEmpty());

        studentDAO.remove(student);
    }

    @Test
    void shouldReturnSameDataWhenCourseNameIsSpecified() throws DaoException {
        List<Student> listOfStudents = new ArrayList<>();
        listOfStudents.add(new Student(201, "Andrew", "Armstrong"));
        listOfStudents.add(new Student(201, "Michele", "Anderson"));
        for (Student student : listOfStudents) {
            studentDAO.insert(student);
        }

        List<Student> expectedList = new ArrayList<>();
        List<Student> studentList = new ArrayList<>();
        studentList = studentDAO.getAll();

        for (Student student : studentList) {
            if (student.getLastName().equals("Armstrong") || student.getLastName().equals("Anderson")) {
                expectedList.add(student);
                studentDAO.insertCourse(student, courseDAO.findByName(ALGEBRA));
            }
        }

        List<Student> actualList = new ArrayList<>();
        actualList = studentDAO.getStudentListByCourseName(ALGEBRA);
        assertEquals(expectedList, actualList);

        Course course = new Course(ALGEBRA, ALGEBRA_DESCRIPTION);
        for (Student student : studentList) {
            if (student.getLastName().equals("Armstrong") || student.getLastName().equals("Anderson")) {
                studentDAO.removeStudentFromCourse(student, course);
                studentDAO.remove(student);
            }
        }
    }

    @Test
    void shouldReturnSameStudentWhenFindingByLastName() throws DaoException {
        Student expectedStudent = new Student(105, 201, "Charles", "Xavier");
        Student actualStudent = studentDAO.findByLastName("Xavier");
        assertNotNull(actualStudent);
        assertEquals(expectedStudent, actualStudent);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenNullIsPassedToInsert() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            studentDAO.insert(null);
        });

        assertEquals(ERROR_MESSAGE, exception.getMessage());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenNullIsPassedToUpdate() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            studentDAO.update(null);
        });

        assertEquals(ERROR_MESSAGE, exception.getMessage());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenNullIsPassedToRemove() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            studentDAO.remove(null);
        });

        assertEquals(ERROR_MESSAGE, exception.getMessage());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenNullIsPassedToInsertCourse() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            studentDAO.insertCourse(null, null);
        });

        assertEquals(ERROR_MESSAGE, exception.getMessage());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenNullIsPassedToRemoveStudent() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            studentDAO.removeStudentFromCourse(null, null);
        });
        assertEquals(ERROR_MESSAGE, exception.getMessage());
    }
}
