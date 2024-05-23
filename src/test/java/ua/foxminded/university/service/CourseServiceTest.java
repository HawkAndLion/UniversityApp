package ua.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import ua.foxminded.university.dao.CourseDAO;
import ua.foxminded.university.dao.DaoException;
import ua.foxminded.university.dao.StudentDAO;
import ua.foxminded.university.data.Course;
import ua.foxminded.university.data.Student;
import ua.foxminded.university.parser.CourseParser;
import ua.foxminded.university.reader.Reader;
import ua.foxminded.university.reader.ReaderException;

class CourseServiceTest {
    private static final String ALGEBRA = "Algebra";
    private static final String ALGEBRA_DESCRIPTION = " Develops algebraic concepts and skills.";
    private static final String ALGEBRA_AND_DESCRIPTION = "Algebra, Develops algebraic concepts and skills.";
    private static final String GEOMETRY = "Geometry";
    private static final String GEOMETRY_DESCRIPTION = "Presents the study of several geometries";
    private static final String FIRST_NAME = "Patrick";
    private static final String LAST_NAME = "Nightgel";
    private static final String FIRST_NAME_STUDENT = "Trisha";
    private static final String LAST_NAME_STUDENT = "Kernel";
    private static final int COURSE_ID = 3;
    private static final int STUDENT_ID = 105;
    private static final int MIN_COURSES_PER_STUDENT = 1;
    private static final int MAX_COURSES_PER_STUDENT = 1;
    private static final int MIN_NUMBER_OF_COURSES = 1;
    private static final int MAX_NUMBER_OF_COURSES = 1;
    private static final String ERROR_MESSAGE = "Argument cannot be null or empty ";
    private static final String GEOGRAPHY = "Geography";
    private static final String GEOGRAPHY_DESCRIPTION = "An introduction to the earth’s natural environmental systems.";
    private static final String FILE_TO_READ = "majorAndDescriptionTest.txt";
    private static final String CHECK_QUERY = "Check the query or duplicate key value, or Database access ";
    private Reader reader;
    private CourseParser parser;
    private Random random;
    private StudentDAO studentDao;
    private CourseDAO courseDao;

    @InjectMocks
    private CourseService service;

    @BeforeEach
    void setUp() {
        random = Mockito.mock(SecureRandom.class, withSettings().withoutAnnotations());
        studentDao = Mockito.mock(StudentDAO.class);
        courseDao = Mockito.mock(CourseDAO.class);
        reader = Mockito.mock(Reader.class);
        parser = Mockito.mock(CourseParser.class);
        service = new CourseService(random, studentDao, courseDao, reader, parser);
    }

    @Test
    void shouldExecuteInsertCourseMethodWhenVerified() throws ServiceException {
        Student student1 = new Student(FIRST_NAME, LAST_NAME);
        Student student2 = new Student(FIRST_NAME_STUDENT, LAST_NAME_STUDENT);
        Course course1 = new Course(ALGEBRA, ALGEBRA_DESCRIPTION);
        Course course2 = new Course(GEOMETRY, GEOMETRY_DESCRIPTION);
        List<Student> listOfStudents = new ArrayList<>();
        listOfStudents.add(student1);
        listOfStudents.add(student2);
        List<Course> listOfCourses = new ArrayList<>();
        listOfCourses.add(course1);
        listOfCourses.add(course2);

        try {
            doNothing().when(studentDao).insertCourse(listOfStudents.get(1), listOfCourses.get(1));
            service.assignCoursesToStudent(listOfStudents, listOfCourses, MIN_COURSES_PER_STUDENT,
                    MAX_COURSES_PER_STUDENT, MIN_NUMBER_OF_COURSES, MAX_NUMBER_OF_COURSES);
            verify(studentDao).insertCourse(listOfStudents.get(1), listOfCourses.get(1));
        } catch (DaoException exception) {
            throw new ServiceException(CHECK_QUERY, exception);
        }
    }

    @Test
    void shouldExecuteMethodWhenVerified() throws ServiceException {
        Student student = new Student(STUDENT_ID, FIRST_NAME, LAST_NAME);
        Course course = new Course(COURSE_ID, ALGEBRA, ALGEBRA_DESCRIPTION);
        try {
            doNothing().when(studentDao).insertCourse(student, course);
            when(courseDao.getById(COURSE_ID)).thenReturn(course);
            when(studentDao.getById(STUDENT_ID)).thenReturn(student);
            service.insertCourseByStudentId(COURSE_ID, STUDENT_ID);
            verify(courseDao).getById(COURSE_ID);
            verify(studentDao).getById(STUDENT_ID);
            verify(studentDao).insertCourse(student, course);
        } catch (DaoException exception) {
            throw new ServiceException(CHECK_QUERY, exception);
        }
    }

    @Test
    void shouldExecuteServiceMethodWhenVerified() throws ServiceException {
        Course course = new Course(COURSE_ID, ALGEBRA, ALGEBRA_DESCRIPTION);
        List<Course> courseListByStudent = new ArrayList<>();
        courseListByStudent.add(course);

        try {
            when(courseDao.findCoursesByStudentId(STUDENT_ID)).thenReturn(courseListByStudent);
            service.getCourseListByStudentId(STUDENT_ID);
            verify(courseDao).findCoursesByStudentId(STUDENT_ID);
        } catch (DaoException exception) {
            throw new ServiceException(CHECK_QUERY, exception);
        }
    }

    @Test
    void shouldExecuteRemoveStudentMethodWhenVerified() throws ServiceException {
        try {
            doNothing().when(courseDao).removeStudentFromCourse(STUDENT_ID, COURSE_ID);
            service.removeStudentFromCourseList(STUDENT_ID, COURSE_ID);
            verify(courseDao).removeStudentFromCourse(STUDENT_ID, COURSE_ID);
        } catch (DaoException exception) {
            throw new ServiceException(CHECK_QUERY, exception);
        }
    }

    @Test
    void shouldExecuteServiceMethodWhenCaseIsVerified() throws ServiceException {
        List<Student> expectedList = new ArrayList<>();
        expectedList.add(new Student(105, 201, "Charles", "Xavier"));
        try {
            when(studentDao.getStudentListByCourseName(ALGEBRA)).thenReturn(expectedList);
            service.getStudentListByCourseName(ALGEBRA);
            verify(studentDao).getStudentListByCourseName(ALGEBRA);
        } catch (DaoException exception) {
            throw new ServiceException(CHECK_QUERY, exception);
        }
    }

    @Test
    void shouldReturnCorrectOrderWhenOrderOfMethodsIsVerified() throws ServiceException {
        Student student = new Student(STUDENT_ID, FIRST_NAME, LAST_NAME);
        Course course = new Course(COURSE_ID, ALGEBRA, ALGEBRA_DESCRIPTION);
        try {
            doNothing().when(studentDao).insertCourse(student, course);
            when(courseDao.getById(COURSE_ID)).thenReturn(course);
            when(studentDao.getById(STUDENT_ID)).thenReturn(student);
            service.insertCourseByStudentId(COURSE_ID, STUDENT_ID);
            InOrder inOrder = Mockito.inOrder(courseDao, studentDao, studentDao);
            inOrder.verify(courseDao).getById(COURSE_ID);
            inOrder.verify(studentDao).getById(STUDENT_ID);
            inOrder.verify(studentDao).insertCourse(student, course);
        } catch (DaoException exception) {
            throw new ServiceException(CHECK_QUERY, exception);
        }
    }

    @Test
    void shouldExecuteMethodOfGettingListWhenVerified() throws ServiceException {
        List<Course> expectedList = new ArrayList<>();
        expectedList.add(new Course(1, ALGEBRA, ALGEBRA_DESCRIPTION));
        expectedList.add(new Course(2, GEOMETRY, GEOMETRY_DESCRIPTION));
        expectedList.add(new Course(3, GEOGRAPHY, GEOGRAPHY_DESCRIPTION));
        try {
            when(courseDao.getAll()).thenReturn(expectedList);
            service.getListOfCoursesFromDatabase();
            verify(courseDao).getAll();
        } catch (DaoException exception) {
            throw new ServiceException(CHECK_QUERY, exception);
        }
    }

    @Test
    void shouldExecuteMethodsWhenVerified() throws ServiceException {
        List<String> list = new ArrayList<>();
        list.add(ALGEBRA_AND_DESCRIPTION);
        List<Course> expectedList = new ArrayList<>();
        expectedList.add(new Course(ALGEBRA, ALGEBRA_DESCRIPTION));
        Course course = new Course(ALGEBRA, ALGEBRA_DESCRIPTION);
        try {
            when(reader.readFile(FILE_TO_READ)).thenReturn(list);
            when(parser.extractListOfCourses(list)).thenReturn(expectedList);
            when(courseDao.insert(course)).thenReturn(expectedList.get(0));
            when(courseDao.getAll()).thenReturn(expectedList);
            service.getListOfCourses(FILE_TO_READ);
            verify(reader).readFile(FILE_TO_READ);
            verify(parser).extractListOfCourses(list);
            verify(courseDao, times(1)).insert(course);
            verify(courseDao, times(1)).getAll();
        } catch (ReaderException exception) {
            throw new ServiceException(ERROR_MESSAGE, exception);
        } catch (DaoException exception) {
            throw new ServiceException(CHECK_QUERY, exception);
        }
    }

    @Test
    void shouldReturnCorrectOrderWhenOrderIsVerified() throws ServiceException {
        List<String> list = new ArrayList<>();
        list.add(ALGEBRA_AND_DESCRIPTION);
        List<Course> expectedList = new ArrayList<>();
        expectedList.add(new Course(ALGEBRA, ALGEBRA_DESCRIPTION));
        Course course = new Course(ALGEBRA, ALGEBRA_DESCRIPTION);
        try {
            when(reader.readFile(FILE_TO_READ)).thenReturn(list);
            when(parser.extractListOfCourses(list)).thenReturn(expectedList);
            when(courseDao.insert(course)).thenReturn(expectedList.get(0));
            when(courseDao.getAll()).thenReturn(expectedList);
            service.getListOfCourses(FILE_TO_READ);
            InOrder inOrder = Mockito.inOrder(reader, parser, courseDao);
            inOrder.verify(reader).readFile(FILE_TO_READ);
            inOrder.verify(parser).extractListOfCourses(list);
            inOrder.verify(courseDao, times(1)).insert(course);
            inOrder.verify(courseDao).getAll();
            inOrder.verifyNoMoreInteractions();
        } catch (ReaderException exception) {
            throw new ServiceException(ERROR_MESSAGE, exception);
        } catch (DaoException exception) {
            throw new ServiceException(CHECK_QUERY, exception);
        }
    }

    @Test
    void shouldExecuteMethodWhenCaseIsVerified() throws ServiceException {
        Course course = new Course(COURSE_ID, ALGEBRA, ALGEBRA_DESCRIPTION);
        try {
            when(courseDao.getById(COURSE_ID)).thenReturn(course);
            service.getCourseById(COURSE_ID);
            verify(courseDao, times(1)).getById(COURSE_ID);
        } catch (DaoException exception) {
            throw new ServiceException(CHECK_QUERY, exception);
        }
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenNullIsPassedToTheMethod() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.getListOfCourses(null);
        });
        assertEquals(ERROR_MESSAGE, exception.getMessage());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenNullIsPassed() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.assignCoursesToStudent(null, null, MIN_COURSES_PER_STUDENT, MAX_COURSES_PER_STUDENT,
                    MIN_NUMBER_OF_COURSES, MAX_NUMBER_OF_COURSES);
        });
        assertEquals(ERROR_MESSAGE, exception.getMessage());
    }
}
