package ua.foxminded.university.ui;

import java.util.List;

import lombok.extern.java.Log;
import ua.foxminded.university.data.Course;
import ua.foxminded.university.data.Group;
import ua.foxminded.university.data.Student;
import ua.foxminded.university.service.CourseService;
import ua.foxminded.university.service.GroupService;
import ua.foxminded.university.service.ServiceException;
import ua.foxminded.university.service.StudentService;

@Log
public class Facade {
    private static final String FIRST_NAMES = "firstNames.txt";
    private static final String LAST_NAMES = "lastNames.txt";
    private static final int NUMBER_OF_GROUPS = 10;
    private static final int MIN_NUMBER_OF_STUDENTS_IN_GROUP = 10;
    private static final int MAX_NUMBER_OF_STUDENTS_IN_GROUP = 30;
    private static final int TOTAL_NUMBER_OF_STUDENTS = 200;
    private static final String MAJOR_AND_DESCRIPTION = "majorAndDescription.txt";
    private static final int MIN_COURSES_PER_STUDENT = 1;
    private static final int MAX_COURSES_PER_STUDENT = 4;
    private static final int MIN_NUMBER_OF_COURSES = 0;
    private static final int MAX_NUMBER_OF_COURSES = 10;
    private static final String CHECK_INFO = "Please check the student's ID and course ID.";
    private static final String REMOVED_INFO = "Student with specified ID was removed from the course.";
    private static final String ERROR_MESSAGE = "The list should not be empty ";
    private StudentService studentService;
    private CourseService courseService;
    private GroupService groupService;

    public Facade(StudentService studentService, CourseService courseService, GroupService groupService) {
        this.studentService = studentService;
        this.courseService = courseService;
        this.groupService = groupService;
    }

    public List<Course> getCourseListByStudentId(int courseId, int studentId) throws ServiceException {
        List<Course> courseList;
        courseService.insertCourseByStudentId(courseId, studentId);
        courseList = courseService.getCourseListByStudentId(studentId);
        return courseList;
    }

    public List<Course> getListOfCourses() throws ServiceException {
        return courseService.getListOfCoursesFromDatabase();
    }

    public List<Student> getUpdatedListOfStudents() throws ServiceException {
        return studentService.getUpdatedListOfStudents();
    }

    public void addNewStudent(String firstName, String lastName) throws ServiceException {
        studentService.addNewStudentToList(firstName, lastName);
    }

    public void removeStudentFromCourse(int studentId, int courseId) throws ServiceException {
        int increment = 0;
        List<Course> listOfCourses;
        listOfCourses = courseService.getCourseListByStudentId(studentId);

        for (int i = 0; i < listOfCourses.size(); i++) {
            courseService.removeStudentFromCourseList(studentId, courseId);
            increment++;
        }

        if (increment == 0) {
            log.info(CHECK_INFO);
        } else {
            log.info(REMOVED_INFO);
        }
    }

    public void deleteStudentById(int studentId) throws ServiceException {
        studentService.removeStudentFromList(studentId);
    }

    public List<Student> formatStudentReport(List<Group> listOfGroups) throws ServiceException {
        List<Student> studentList;
        List<Student> newStudentList;

        if (listOfGroups.isEmpty()) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }

        studentList = studentService.getListOfStudents(FIRST_NAMES, LAST_NAMES, TOTAL_NUMBER_OF_STUDENTS);
        newStudentList = groupService.assignStudentsToGroups(studentList, listOfGroups,
                MIN_NUMBER_OF_STUDENTS_IN_GROUP, MAX_NUMBER_OF_STUDENTS_IN_GROUP);
        return newStudentList;
    }

    public List<Group> formatGroupReport() throws ServiceException {
        return groupService.getListOfGroups(NUMBER_OF_GROUPS);
    }

    public List<Course> formatCourseReport(List<Student> listOfStudents) throws ServiceException {
        if (listOfStudents.isEmpty()) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }

        List<Course> coursesList;
        coursesList = courseService.getListOfCourses(MAJOR_AND_DESCRIPTION);
        courseService.assignCoursesToStudent(listOfStudents, coursesList, MIN_COURSES_PER_STUDENT,
                MAX_COURSES_PER_STUDENT, MIN_NUMBER_OF_COURSES, MAX_NUMBER_OF_COURSES);
        return coursesList;
    }

    public List<Group> getReportOnGroupsWithStudentAmount(int inputNumber, List<Student> listOfStudents,
            List<Group> listOfGroups) {
        if (listOfStudents.isEmpty() || listOfGroups.isEmpty()) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }

        List<Group> groupList;
        groupList = groupService.findGroups(inputNumber, listOfStudents, listOfGroups);
        return groupList;
    }

    public List<Student> getListOfStudentsByCourseName(int courseId) throws ServiceException {
        Course course = courseService.getCourseById(courseId);
        List<Student> list;
        list = courseService.getStudentListByCourseName(course.getName());
        return list;
    }
}
