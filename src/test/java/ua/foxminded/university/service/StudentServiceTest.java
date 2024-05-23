package ua.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import ua.foxminded.university.dao.CourseDAO;
import ua.foxminded.university.dao.DaoException;
import ua.foxminded.university.dao.StudentDAO;
import ua.foxminded.university.data.Course;
import ua.foxminded.university.data.Student;
import ua.foxminded.university.generator.StudentGenerator;
import ua.foxminded.university.reader.Reader;
import ua.foxminded.university.reader.ReaderException;

class StudentServiceTest {
    private static final String FIRST_NAME = "Jessica";
    private static final String LAST_NAME = "Parker";
    private static final String COURSE = "Math";
    private static final String COURSE_DESCRIPTION = "Algebra + Geometry";
    private static final String FIRST_NAMES_FILE = "firstNamesTest.txt";
    private static final String LAST_NAMES_FILE = "lastNamesTest.txt";
    private static final int NUMBER_OF_STUDENTS = 1;
    private static final String CHECK_QUERY = "Check the query or duplicate key value, or Database access ";
    private static final String ERROR_MESSAGE = "Please check pathname or access rights ";
    private static final String NULL_ERROR_MESSAGE = "Argument cannot be null ";
    private StudentDAO studentDao;
    private CourseDAO courseDao;
    private Reader reader;
    private StudentGenerator generator;

    @InjectMocks
    private StudentService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        studentDao = Mockito.mock(StudentDAO.class);
        courseDao = Mockito.mock(CourseDAO.class);
        reader = Mockito.mock(Reader.class);
        generator = Mockito.mock(StudentGenerator.class);
        service = new StudentService(studentDao, courseDao, generator, reader);
    }

    @Test
    void shouldExecuteMethodWhenVerified() throws ServiceException {
        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student("Patricia", "Parks"));
        studentList.add(new Student("Keanu", "Reeves"));

        try {
            when(studentDao.getAll()).thenReturn(studentList);
            service.getUpdatedListOfStudents();
            verify(studentDao, times(1)).getAll();
        } catch (DaoException exception) {
            throw new ServiceException(CHECK_QUERY, exception);
        }
    }

    @Test
    void shouldExecuteMethodOfAddingStudentWhenVerified() throws ServiceException {
        try {
            List<Student> studentList = new ArrayList<>();
            Student student = new Student(FIRST_NAME, LAST_NAME);
            studentList.add(student);

            when(studentDao.insert(studentList.get(0))).thenReturn(student);
            when(studentDao.getAll()).thenReturn(studentList);

            service.addNewStudentToList(FIRST_NAME, LAST_NAME);

            verify(studentDao, times(1)).insert(student);
            verify(studentDao, times(1)).getAll();
        } catch (DaoException exception) {
            throw new ServiceException(CHECK_QUERY, exception);
        }
    }

    @Test
    void shouldExecuteMethodOfStudentRemovalWhenVerified() throws ServiceException {
        try {
            List<Student> studentList = new ArrayList<>();
            Student student = new Student(1, 205, FIRST_NAME, LAST_NAME);
            studentList.add(student);
            Course course = new Course(5, COURSE, COURSE_DESCRIPTION);
            List<Course> courseList = new ArrayList<>();
            courseList.add(course);

            when(studentDao.getById(student.getStudentId())).thenReturn(student);
            when(courseDao.findCoursesByStudentId(student.getStudentId())).thenReturn(courseList);
            when(studentDao.removeStudentFromCourse(student, course)).thenReturn(student);

            service.removeStudentFromList(student.getStudentId());

            verify(studentDao, times(1)).getById(student.getStudentId());
            verify(courseDao, times(1)).findCoursesByStudentId(student.getStudentId());
            verify(studentDao, times(1)).removeStudentFromCourse(student, course);
        } catch (DaoException exception) {
            throw new ServiceException(CHECK_QUERY, exception);
        }
    }

    @Test
    void shouldReturnCorrectOrderWhenOrderIsVerified() throws ServiceException {
        try {
            List<Student> studentList = new ArrayList<>();
            Student student = new Student(1, 205, FIRST_NAME, LAST_NAME);
            studentList.add(student);
            Course course = new Course(5, COURSE, COURSE_DESCRIPTION);
            List<Course> courseList = new ArrayList<>();
            courseList.add(course);

            when(studentDao.getById(student.getStudentId())).thenReturn(student);
            when(courseDao.findCoursesByStudentId(student.getStudentId())).thenReturn(courseList);
            when(studentDao.removeStudentFromCourse(student, course)).thenReturn(student);

            service.removeStudentFromList(student.getStudentId());

            InOrder inOrder = Mockito.inOrder(studentDao, courseDao, studentDao);
            inOrder.verify(studentDao).getById(student.getStudentId());
            inOrder.verify(courseDao).findCoursesByStudentId(student.getStudentId());
            inOrder.verify(studentDao).removeStudentFromCourse(student, course);
        } catch (DaoException exception) {
            throw new ServiceException(CHECK_QUERY, exception);
        }
    }

    @Test
    void shouldReturnCorrectOrderOfMethodsWhenVerified() throws ServiceException {
        List<String> firstNamesList = new ArrayList<>();
        firstNamesList.add("Patrick");
        List<String> lastNamesList = new ArrayList<>();
        lastNamesList.add("Rosenburg");
        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student("Patrick", "Rosenburg"));
        Student student = new Student("Patrick", "Rosenburg");
        try {
            when(reader.readFile(FIRST_NAMES_FILE)).thenReturn(firstNamesList);
            when(reader.readFile(LAST_NAMES_FILE)).thenReturn(lastNamesList);
            when(studentDao.insert(studentList.get(0))).thenReturn(student);
            when(studentDao.getAll()).thenReturn(studentList);

            service.getListOfStudents(FIRST_NAMES_FILE, LAST_NAMES_FILE, NUMBER_OF_STUDENTS);

            InOrder inOrder = Mockito.inOrder(reader, studentDao);
            inOrder.verify(reader).readFile(FIRST_NAMES_FILE);
            inOrder.verify(reader).readFile(LAST_NAMES_FILE);
            inOrder.verify(studentDao).insert(studentList.get(0));
            inOrder.verify(studentDao).getAll();
        } catch (ReaderException exception) {
            throw new ServiceException(ERROR_MESSAGE, exception);
        } catch (DaoException exception) {
            throw new ServiceException(CHECK_QUERY, exception);
        }
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenNullIsPassed() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.getListOfStudents(null, null, 0);
        });
        assertEquals(NULL_ERROR_MESSAGE, exception.getMessage());
    }
}
