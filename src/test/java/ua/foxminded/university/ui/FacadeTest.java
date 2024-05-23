package ua.foxminded.university.ui;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.foxminded.university.data.Course;
import ua.foxminded.university.data.Group;
import ua.foxminded.university.data.Student;
import ua.foxminded.university.service.CourseService;
import ua.foxminded.university.service.GroupService;
import ua.foxminded.university.service.ServiceException;
import ua.foxminded.university.service.StudentService;

@ExtendWith(MockitoExtension.class)
class FacadeTest {
    private static final String EXPECTED_MESSAGE = "The list should not be empty ";
    private static final int ALGEBRA_ID = 1;
    private static final String ALGEBRA_MAJOR = "Algebra";
    private static final String ALGEBRA_DESCRIPTION = "Develops algebraic concepts and skills.";
    private static final String GEOMETRY_MAJOR = "Geometry";
    private static final String GEOMETRY_DESCRIPTION = "Presents the study of several geometries.";
    private static final String GEOGRAPHY_MAJOR = "Geography";
    private static final String GEOGRAPHY_DESCRIPTION = "An introduction to the earth’s natural environmental systems.";
    private static final int COURSE_ID = 5;
    private static final int STUDENT_ID = 127;
    private static final String FIRST_NAME = "Patricia";
    private static final String LAST_NAME = "Parker";
    private static final String FIRST_NAMES = "firstNames.txt";
    private static final String LAST_NAMES = "lastNames.txt";
    private static final int TOTAL_NUMBER_OF_STUDENTS = 200;
    private static final int MIN_NUMBER_OF_STUDENTS_IN_GROUP = 10;
    private static final int MAX_NUMBER_OF_STUDENTS_IN_GROUP = 30;
    private static final int NUMBER_OF_GROUPS = 10;
    private static final String MAJOR_AND_DESCRIPTION = "majorAndDescription.txt";
    private static final int MIN_COURSES_PER_STUDENT = 1;
    private static final int MAX_COURSES_PER_STUDENT = 4;
    private static final int MIN_NUMBER_OF_COURSES = 0;
    private static final int MAX_NUMBER_OF_COURSES = 10;
    private static final int INPUT_NUMBER = 123;
    private StudentService studentService;
    private CourseService courseService;
    private GroupService groupService;

    @InjectMocks
    private Facade facade;

    @BeforeEach
    void setUp() {
        studentService = Mockito.mock(StudentService.class);
        courseService = Mockito.mock(CourseService.class);
        groupService = Mockito.mock(GroupService.class);
        facade = new Facade(studentService, courseService, groupService);
    }

    @Test
    void shouldExecuteOnceWhenVerified() throws ServiceException {
        List<Course> expectedList = new ArrayList<>();
        Course course1 = new Course(ALGEBRA_MAJOR, ALGEBRA_DESCRIPTION);
        Course course2 = new Course(GEOMETRY_MAJOR, GEOMETRY_DESCRIPTION);
        Course course3 = new Course(GEOGRAPHY_MAJOR, GEOGRAPHY_DESCRIPTION);
        expectedList.add(course1);
        expectedList.add(course2);
        expectedList.add(course3);

        when(courseService.getListOfCoursesFromDatabase()).thenReturn(expectedList);
        facade.getListOfCourses();

        verify(courseService, times(1)).getListOfCoursesFromDatabase();
    }

    @Test
    void shouldExecuteOnceWhenMethodIsVerified() throws ServiceException {
        List<Course> expectedList = new ArrayList<>();
        Course course1 = new Course(ALGEBRA_MAJOR, ALGEBRA_DESCRIPTION);
        Course course2 = new Course(GEOMETRY_MAJOR, GEOMETRY_DESCRIPTION);
        Course course3 = new Course(GEOGRAPHY_MAJOR, GEOGRAPHY_DESCRIPTION);
        expectedList.add(course1);
        expectedList.add(course2);
        expectedList.add(course3);

        when(courseService.getCourseListByStudentId(STUDENT_ID)).thenReturn(expectedList);
        facade.getCourseListByStudentId(COURSE_ID, STUDENT_ID);
        verify(courseService, times(1)).getCourseListByStudentId(STUDENT_ID);
    }

    @Test
    void shouldExecuteOnceWhenFacadeMethodIsVerified() throws ServiceException {
        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student(123, "Alicia", "Watson"));
        studentList.add(new Student(124, "Byanka", "Wallmart"));
        when(studentService.getUpdatedListOfStudents()).thenReturn(studentList);
        facade.getUpdatedListOfStudents();
        verify(studentService, times(1)).getUpdatedListOfStudents();
    }

    @Test
    void shouldExecuteAtMostWhenMethodIsVerified() throws ServiceException {
        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student(123, "Alicia", "Watson"));
        studentList.add(new Student(124, "Jeremy", "Terr"));
        studentList.add(new Student(125, "Patricia", "Parker"));
        when(studentService.addNewStudentToList(FIRST_NAME, LAST_NAME)).thenReturn(studentList);
        facade.addNewStudent(FIRST_NAME, LAST_NAME);
        verify(studentService, atMost(1)).addNewStudentToList(FIRST_NAME, LAST_NAME);
    }

    @Test
    void shouldExecuteMethodWhenCaseIsVerified() throws ServiceException {
        List<Course> listOfCourses = new ArrayList<>();
        Course course1 = new Course(ALGEBRA_MAJOR, ALGEBRA_DESCRIPTION);
        Course course2 = new Course(GEOMETRY_MAJOR, GEOMETRY_DESCRIPTION);
        Course course3 = new Course(GEOGRAPHY_MAJOR, GEOGRAPHY_DESCRIPTION);
        listOfCourses.add(course1);
        listOfCourses.add(course2);
        listOfCourses.add(course3);

        when(courseService.getCourseListByStudentId(STUDENT_ID)).thenReturn(listOfCourses);
        facade.removeStudentFromCourse(STUDENT_ID, COURSE_ID);
        verify(courseService).getCourseListByStudentId(STUDENT_ID);
    }

    @Test
    void shouldExecuteStudentRemovalWhenVerified() throws ServiceException {
        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student(123, "Alicia", "Watson"));
        studentList.add(new Student(124, "Jeremy", "Terr"));
        studentList.add(new Student(125, "Patricia", "Parker"));

        when(studentService.removeStudentFromList(STUDENT_ID)).thenReturn(studentList);
        facade.deleteStudentById(STUDENT_ID);

        verify(studentService).removeStudentFromList(STUDENT_ID);
    }

    @Test
    void shouldExecuteAtMostWhenEntitiesAreVerified() throws ServiceException {
        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student("Alicia", "Watson"));
        studentList.add(new Student("Jeremy", "Terr"));
        studentList.add(new Student("Patricia", "Parker"));

        List<Student> studentListWithGroups = new ArrayList<>();
        studentListWithGroups.add(new Student(123, "Alicia", "Watson"));
        studentListWithGroups.add(new Student(124, "Jeremy", "Terr"));
        studentListWithGroups.add(new Student(125, "Patricia", "Parker"));

        List<Group> listOfGroups = new ArrayList<>();
        Group group1 = new Group(123, "HQ-12");
        Group group2 = new Group(124, "KL-28");
        Group group3 = new Group(125, "JR-57");
        listOfGroups.add(group1);
        listOfGroups.add(group2);
        listOfGroups.add(group3);

        when(studentService.getListOfStudents(FIRST_NAMES, LAST_NAMES, TOTAL_NUMBER_OF_STUDENTS))
                .thenReturn(studentList);
        when(groupService.assignStudentsToGroups(studentList, listOfGroups, MIN_NUMBER_OF_STUDENTS_IN_GROUP,
                MAX_NUMBER_OF_STUDENTS_IN_GROUP)).thenReturn(studentListWithGroups);
        facade.formatStudentReport(listOfGroups);
        verify(studentService, atMost(1)).getListOfStudents(FIRST_NAMES, LAST_NAMES, TOTAL_NUMBER_OF_STUDENTS);
        verify(groupService, atMost(1)).assignStudentsToGroups(studentList, listOfGroups,
                MIN_NUMBER_OF_STUDENTS_IN_GROUP, MAX_NUMBER_OF_STUDENTS_IN_GROUP);
    }

    @Test
    void shouldExecuteMethodWhenItIsVerified() throws ServiceException {
        List<Group> listOfGroups = new ArrayList<>();
        Group group1 = new Group(123, "HQ-12");
        Group group2 = new Group(124, "KL-28");
        Group group3 = new Group(125, "JR-57");
        listOfGroups.add(group1);
        listOfGroups.add(group2);
        listOfGroups.add(group3);

        when(groupService.getListOfGroups(NUMBER_OF_GROUPS)).thenReturn(listOfGroups);
        facade.formatGroupReport();
        verify(groupService).getListOfGroups(NUMBER_OF_GROUPS);
    }

    @Test
    void shouldExecuteOnceWhenEntitiesAreVerified() throws ServiceException {
        List<Course> expectedList = new ArrayList<>();
        Course course1 = new Course(ALGEBRA_MAJOR, ALGEBRA_DESCRIPTION);
        Course course2 = new Course(GEOMETRY_MAJOR, GEOMETRY_DESCRIPTION);
        Course course3 = new Course(GEOGRAPHY_MAJOR, GEOGRAPHY_DESCRIPTION);
        expectedList.add(course1);
        expectedList.add(course2);
        expectedList.add(course3);

        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student("Alicia", "Watson"));
        studentList.add(new Student("Jeremy", "Terr"));
        studentList.add(new Student("Patricia", "Parker"));

        when(courseService.getListOfCourses(MAJOR_AND_DESCRIPTION)).thenReturn(expectedList);
        doNothing().when(courseService).assignCoursesToStudent(studentList, expectedList, MIN_COURSES_PER_STUDENT,
                MAX_COURSES_PER_STUDENT, MIN_NUMBER_OF_COURSES, MAX_NUMBER_OF_COURSES);
        facade.formatCourseReport(studentList);
        verify(courseService).getListOfCourses(MAJOR_AND_DESCRIPTION);
        verify(courseService, times(1)).assignCoursesToStudent(studentList, expectedList, MIN_COURSES_PER_STUDENT,
                MAX_COURSES_PER_STUDENT, MIN_NUMBER_OF_COURSES, MAX_NUMBER_OF_COURSES);
    }

    @Test
    void shouldExecuteMethodWhenGroupServiceIsVerified() {
        List<Group> listOfGroups = new ArrayList<>();
        Group group1 = new Group(123, "HQ-12");
        Group group2 = new Group(124, "KL-28");
        Group group3 = new Group(125, "JR-57");
        listOfGroups.add(group1);
        listOfGroups.add(group2);
        listOfGroups.add(group3);

        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student(123, "Alicia", "Watson"));
        studentList.add(new Student(124, "Katlyn", "Preston"));
        studentList.add(new Student(123, "Byanka", "Wallmart"));

        List<Group> groupList = new ArrayList<>();
        Group groupList1 = new Group(123, "HQ-12");
        Group groupList2 = new Group(123, "HQ-12");
        listOfGroups.add(groupList1);
        listOfGroups.add(groupList2);

        when(groupService.findGroups(INPUT_NUMBER, studentList, listOfGroups)).thenReturn(groupList);
        facade.getReportOnGroupsWithStudentAmount(INPUT_NUMBER, studentList, listOfGroups);
        verify(groupService, times(1)).findGroups(INPUT_NUMBER, studentList, listOfGroups);

    }

    @Test
    void shouldExecuteMethodsWhenCourseServiceIsVerified() throws ServiceException {
        List<Student> listOfStudents = new ArrayList<>();
        listOfStudents.add(new Student("Alicia", "Watson"));
        listOfStudents.add(new Student("Jeremy", "Terr"));
        Course course = new Course(ALGEBRA_MAJOR, ALGEBRA_DESCRIPTION);

        when(courseService.getCourseById(ALGEBRA_ID)).thenReturn(course);
        when(courseService.getStudentListByCourseName(ALGEBRA_MAJOR)).thenReturn(listOfStudents);
        facade.getListOfStudentsByCourseName(ALGEBRA_ID);
        verify(courseService).getCourseById(ALGEBRA_ID);
        verify(courseService).getStudentListByCourseName(ALGEBRA_MAJOR);
    }

    @Test
    void shouldReturnCorrectOrderWhenOrderIsVerified() throws ServiceException {
        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student("Alicia", "Watson"));
        studentList.add(new Student("Jeremy", "Terr"));
        studentList.add(new Student("Patricia", "Parker"));

        List<Student> studentListWithGroups = new ArrayList<>();
        studentListWithGroups.add(new Student(123, "Alicia", "Watson"));
        studentListWithGroups.add(new Student(124, "Jeremy", "Terr"));
        studentListWithGroups.add(new Student(125, "Patricia", "Parker"));

        List<Group> listOfGroups = new ArrayList<>();
        Group group1 = new Group(123, "HQ-12");
        Group group2 = new Group(124, "KL-28");
        Group group3 = new Group(125, "JR-57");
        listOfGroups.add(group1);
        listOfGroups.add(group2);
        listOfGroups.add(group3);
        when(studentService.getListOfStudents(FIRST_NAMES, LAST_NAMES, TOTAL_NUMBER_OF_STUDENTS))
                .thenReturn(studentList);
        when(groupService.assignStudentsToGroups(studentList, listOfGroups, MIN_NUMBER_OF_STUDENTS_IN_GROUP,
                MAX_NUMBER_OF_STUDENTS_IN_GROUP)).thenReturn(studentListWithGroups);
        facade.formatStudentReport(listOfGroups);

        InOrder inOrder = Mockito.inOrder(studentService, groupService);

        inOrder.verify(studentService).getListOfStudents(FIRST_NAMES, LAST_NAMES, TOTAL_NUMBER_OF_STUDENTS);
        inOrder.verify(groupService).assignStudentsToGroups(studentList, listOfGroups, MIN_NUMBER_OF_STUDENTS_IN_GROUP,
                MAX_NUMBER_OF_STUDENTS_IN_GROUP);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldReturnCorrectOrderWhenOrderOfMethodsIsVerified() throws ServiceException {
        List<Course> expectedList = new ArrayList<>();
        Course course1 = new Course(ALGEBRA_MAJOR, ALGEBRA_DESCRIPTION);
        Course course2 = new Course(GEOMETRY_MAJOR, GEOMETRY_DESCRIPTION);
        Course course3 = new Course(GEOGRAPHY_MAJOR, GEOGRAPHY_DESCRIPTION);
        expectedList.add(course1);
        expectedList.add(course2);
        expectedList.add(course3);

        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student("Alicia", "Watson"));
        studentList.add(new Student("Jeremy", "Terr"));
        studentList.add(new Student("Patricia", "Parker"));

        when(courseService.getListOfCourses(MAJOR_AND_DESCRIPTION)).thenReturn(expectedList);
        doNothing().when(courseService).assignCoursesToStudent(studentList, expectedList, MIN_COURSES_PER_STUDENT,
                MAX_COURSES_PER_STUDENT, MIN_NUMBER_OF_COURSES, MAX_NUMBER_OF_COURSES);
        facade.formatCourseReport(studentList);

        InOrder inOrder = Mockito.inOrder(courseService, courseService);
        inOrder.verify(courseService).getListOfCourses(MAJOR_AND_DESCRIPTION);
        inOrder.verify(courseService).assignCoursesToStudent(studentList, expectedList, MIN_COURSES_PER_STUDENT,
                MAX_COURSES_PER_STUDENT, MIN_NUMBER_OF_COURSES, MAX_NUMBER_OF_COURSES);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenGroupListIsEmpty() {
        List<Group> list = new ArrayList<>();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            facade.formatStudentReport(list);
        });

        assertEquals(EXPECTED_MESSAGE, exception.getMessage());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenStudentListIsEmpty() {
        List<Student> listOfStudents = new ArrayList<>();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            facade.formatCourseReport(listOfStudents);
        });

        assertEquals(EXPECTED_MESSAGE, exception.getMessage());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenListsAreEmpty() {
        List<Student> listOfStudents = new ArrayList<>();
        List<Group> list = new ArrayList<>();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            facade.getReportOnGroupsWithStudentAmount(INPUT_NUMBER, listOfStudents, list);
        });

        assertEquals(EXPECTED_MESSAGE, exception.getMessage());
    }
}
