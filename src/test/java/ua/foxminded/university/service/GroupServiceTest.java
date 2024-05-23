package ua.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.*;
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
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import ua.foxminded.university.dao.DaoException;
import ua.foxminded.university.dao.GroupDAO;
import ua.foxminded.university.dao.StudentDAO;
import ua.foxminded.university.data.Group;
import ua.foxminded.university.data.Student;
import ua.foxminded.university.generator.GroupGenerator;

class GroupServiceTest {
    private static final String FIRST_NAME = "Veronica";
    private static final String LAST_NAME = "Castro";
    private static final String GROUP_NAME = "HK-15";
    private static final int MIN_NUMBER_OF_STUDENTS = 1;
    private static final int MAX_NUMBER_OF_STUDENTS = 1;
    private static final String ERROR_MESSAGE = "Argument cannot be null or empty ";
    private static final int INPUT_NUMBER = 2;
    private static final String CHECK_QUERY = "Check the query or duplicate key value, or Database access ";
    private StudentDAO studentDao;
    private GroupDAO groupDao;
    private GroupGenerator groupGenerator;
    private Random random;

    @InjectMocks
    private GroupService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        random = Mockito.mock(SecureRandom.class, withSettings().withoutAnnotations());
        studentDao = Mockito.mock(StudentDAO.class);
        groupDao = Mockito.mock(GroupDAO.class);
        groupGenerator = Mockito.mock(GroupGenerator.class);
        service = new GroupService(random, studentDao, groupDao, groupGenerator);
    }

    @Test
    void shouldReturnDataWhenVerified() throws ServiceException {
        Student student = new Student(FIRST_NAME, LAST_NAME);
        List<Student> listOfStudents = new ArrayList<>();
        listOfStudents.add(student);
        List<Group> listOfGroups = new ArrayList<>();
        listOfGroups.add(new Group(GROUP_NAME));

        try {
            when(studentDao.getAll()).thenReturn(listOfStudents);

            service.assignStudentsToGroups(listOfStudents, listOfGroups, MIN_NUMBER_OF_STUDENTS,
                    MAX_NUMBER_OF_STUDENTS);

            verify(studentDao).getAll();
        } catch (DaoException exception) {
            throw new ServiceException(CHECK_QUERY, exception);
        }
    }

    @Test
    void shouldReturnSameGroupListWhenValuesAreCompared() {
        List<Group> groupList = new ArrayList<>();
        groupList.add(new Group(3, "WO-25"));
        groupList.add(new Group(7, "MK-43"));
        groupList.add(new Group(10, "UY-18"));
        List<Group> expectedList = new ArrayList<>();
        expectedList.add(new Group(3, "WO-25"));
        expectedList.add(new Group(7, "MK-43"));
        expectedList.add(new Group(10, "UY-18"));
        expectedList.add(new Group(0, "No name"));
        List<Group> actualList = new ArrayList<>();

        List<Student> listOfStudents = new ArrayList<>();
        listOfStudents.add(new Student(3, "Jeniffer", "Lopes"));
        listOfStudents.add(new Student(3, "Syntia", "Kendrich"));
        listOfStudents.add(new Student(4, "Patrick", "Noah"));
        listOfStudents.add(new Student(4, "Stacey", "Lasely"));
        listOfStudents.add(new Student(4, "Brandon", "Ocean"));
        listOfStudents.add(new Student(4, "Linda", "Brown"));
        listOfStudents.add(new Student(7, "Daniel", "Craig"));
        listOfStudents.add(new Student(8, "Johnathan", "Staple"));
        listOfStudents.add(new Student(8, "Ken", "Kimberly"));
        listOfStudents.add(new Student(8, "Erica", "Snow"));
        listOfStudents.add(new Student(10, "Jeffrey", "Kendle"));
        listOfStudents.add(new Student(10, "Bruce", "Allmighty"));

        actualList = service.findGroups(INPUT_NUMBER, listOfStudents, groupList);
        assertEquals(expectedList, actualList);
    }

    @Test
    void shouldReturnListWhenVerified() throws ServiceException {
        Group group = new Group(GROUP_NAME);
        List<Group> listOfGroups = new ArrayList<>();
        listOfGroups.add(group);
        try {
            when(groupDao.getAll()).thenReturn(listOfGroups);
            service.getListOfGroups(1);
            verify(groupDao, times(1)).getAll();
        } catch (DaoException exception) {
            throw new ServiceException(CHECK_QUERY, exception);
        }
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenNullIsPassed() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.assignStudentsToGroups(null, null, MIN_NUMBER_OF_STUDENTS, MAX_NUMBER_OF_STUDENTS);
        });
        assertEquals(ERROR_MESSAGE, exception.getMessage());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenNullIsPassedToMethod() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.findGroups(INPUT_NUMBER, null, null);
        });
        assertEquals(ERROR_MESSAGE, exception.getMessage());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenGroupListIsEmpty() {
        List<Group> groupList = new ArrayList<>();
        List<Student> listOfStudents = new ArrayList<>();
        listOfStudents.add(new Student(3, "Jeniffer", "Lopes"));
        listOfStudents.add(new Student(3, "Syntia", "Kendrich"));
        listOfStudents.add(new Student(4, "Patrick", "Noah"));
        listOfStudents.add(new Student(4, "Stacey", "Lasely"));
        listOfStudents.add(new Student(4, "Brandon", "Ocean"));
        listOfStudents.add(new Student(4, "Linda", "Brown"));
        listOfStudents.add(new Student(7, "Daniel", "Craig"));
        listOfStudents.add(new Student(8, "Johnathan", "Staple"));
        listOfStudents.add(new Student(8, "Ken", "Kimberly"));
        listOfStudents.add(new Student(8, "Erica", "Snow"));
        listOfStudents.add(new Student(10, "Jeffrey", "Kendle"));
        listOfStudents.add(new Student(10, "Bruce", "Allmighty"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.findGroups(INPUT_NUMBER, listOfStudents, groupList);
        });
        assertEquals(ERROR_MESSAGE, exception.getMessage());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenStudentListIsEmpty() {
        List<Group> groupList = new ArrayList<>();
        groupList.add(new Group(3, "WO-25"));
        groupList.add(new Group(7, "MK-43"));
        groupList.add(new Group(10, "UY-18"));

        List<Student> listOfStudents = new ArrayList<>();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.findGroups(INPUT_NUMBER, listOfStudents, groupList);
        });
        assertEquals(ERROR_MESSAGE, exception.getMessage());
    }
}
