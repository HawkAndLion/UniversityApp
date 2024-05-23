package ua.foxminded.university.domain;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import lombok.extern.java.Log;
import ua.foxminded.university.data.Course;
import ua.foxminded.university.data.Group;
import ua.foxminded.university.data.Student;
import ua.foxminded.university.service.CourseService;
import ua.foxminded.university.service.GroupService;
import ua.foxminded.university.service.ServiceException;
import ua.foxminded.university.service.StudentService;
import ua.foxminded.university.ui.Facade;

@Log
public class Main {
    private static final String SQL_FILE = "schemaFile.sql";
    private static final String FIND_GROUPS_ACCORDING_TO_NUMBER = "PRESS [1] to Find all groups with less or equals student count ";
    private static final String FIND_STUDENTS_RELATED_TO_COURSE = "PRESS [2] to Find all students related to course with given name ";
    private static final String ADD_NEW_STUDENT = "PRESS [3] to Add new student ";
    private static final String DELETE_STUDENT = "PRESS [4] to Delete student by STUDENT_ID ";
    private static final String ADD_STUDENT_TO_COURSE = "PRESS [5] to Add a student to the course (from a list) ";
    private static final String REMOVE_STUDENT_FROM_COURSE = "PRESS [6] to Remove the student from one of his or her courses ";
    private static final String EXIT = "PRESS [0] to Exit ";
    private static final String ENTER_STUDENT_COUNT = "Enter student count (from 10 to 30) or press 0 to exit: ";
    private static final String WARNING_ONLY_NUMBERS = "Please enter only numbers. ";
    private static final String WARNING_INCORRECT_STUDENT_AMOUNT = "The student amount is incorrect. Try again ";
    private static final String CHOOSE_COURSE = "Choose the course to find students";
    private static final String CONNECTION_MESSAGE = "Database connection successful!";
    private static final String ENTER_FIRST_NAME = "Enter first name: ";
    private static final String ENTER_LAST_NAME = "Enter last name: ";
    private static final String ENTER_STUDENT_ID = "Enter student ID: ";
    private static final String STUDENT_DELETED_MESSAGE = "Student with specified ID was deleted. ";
    private static final String INCORRECT_ID_MEGGASE = "Incorrect student ID. Try again. ";
    private static final String ENTER_COURSE_ID = "Enter course ID from the list to add a student: ";
    private static final String COURSE_ID_TO_REMOVE_STUDENT = "Enter course ID to remove the student: ";
    private static StudentService studentService = new StudentService();
    private static CourseService courseService = new CourseService();
    private static GroupService groupService = new GroupService();
    private static Facade facade = new Facade(studentService, courseService, groupService);

    public static void main(String[] args) throws SQLException, ServiceException {
        List<Course> listOfCourses;
        List<Student> listOfStudents;
        List<Group> listOfGroups;
        ScriptRunner scriptRunner = new ScriptRunner();

        try (Connection connection = ConnectionManager.getConnection();) {
            log.info(CONNECTION_MESSAGE);
            scriptRunner.runSQLScript(SQL_FILE, connection);
        }

        listOfGroups = facade.formatGroupReport();
        listOfStudents = facade.formatStudentReport(listOfGroups);
        listOfCourses = facade.formatCourseReport(listOfStudents);

        try (Scanner scan = new Scanner(System.in)) {
            while (true) {
                print(FIND_GROUPS_ACCORDING_TO_NUMBER);
                print(FIND_STUDENTS_RELATED_TO_COURSE);
                print(ADD_NEW_STUDENT);
                print(DELETE_STUDENT);
                print(ADD_STUDENT_TO_COURSE);
                print(REMOVE_STUDENT_FROM_COURSE);
                print(EXIT);
                int choice = scan.nextInt();
                if (choice == 1) {
                    List<Group> groupList;
                    print(ENTER_STUDENT_COUNT);
                    int input = 0;
                    try {
                        input = scan.nextInt();
                    } catch (InputMismatchException exception) {
                        log.info(WARNING_ONLY_NUMBERS);
                    }

                    if (input >= 10 && input <= 30) {
                        groupList = facade.getReportOnGroupsWithStudentAmount(input, listOfStudents, listOfGroups);

                        for (int i = 0; i < groupList.size(); i++) {
                            System.out.print(i + 1 + ") GROUP ID : " + groupList.get(i).getId() + ", ");
                            print(" GROUP NAME : " + groupList.get(i).getName());

                        }
                    } else if (input < 10 && input > 30) {
                        print(WARNING_INCORRECT_STUDENT_AMOUNT);
                    }
                }
                if (choice == 2) {
                    List<Student> list;
                    print(CHOOSE_COURSE);
                    for (int i = 0; i < listOfCourses.size(); i++) {
                        print("ID: " + listOfCourses.get(i).getId() + ", Course name: " + listOfCourses.get(i).getName()
                                + ", Course Description: " + listOfCourses.get(i).getDescription());
                    }
                    int courseId = scan.nextInt();
                    scan.nextLine();
                    list = facade.getListOfStudentsByCourseName(courseId);
                    for (Student student : list) {
                        print(" Students' ID: " + student.getStudentId() + " Student's name in the course: "
                                + student.getFirstName() + " " + student.getLastName());
                    }
                    printNewLine();
                }

                if (choice == 3) {
                    print(ENTER_FIRST_NAME);
                    scan.nextLine();
                    String firstName = scan.nextLine();

                    print(ENTER_LAST_NAME);
                    String lastName = scan.nextLine();
                    facade.addNewStudent(firstName, lastName);
                }
                if (choice == 4) {
                    print(ENTER_STUDENT_ID);
                    int studentId = scan.nextInt();
                    listOfStudents = facade.getUpdatedListOfStudents();
                    int temporary = 0;
                    for (Student student : listOfStudents) {
                        if (student.getStudentId() == studentId && temporary == 0) {
                            facade.deleteStudentById(studentId);
                            temporary++;
                        }
                    }
                    if (temporary != 0) {
                        log.info(STUDENT_DELETED_MESSAGE);
                    } else if (temporary == 0) {
                        log.info(INCORRECT_ID_MEGGASE);
                    }
                }
                if (choice == 5) {
                    print(ENTER_COURSE_ID);
                    getCourseList();
                    int courseId = scan.nextInt();
                    print(ENTER_STUDENT_ID);
                    int studentId = scan.nextInt();
                    getStudentCourseList(courseId, studentId);
                }
                if (choice == 6) {
                    print(ENTER_STUDENT_ID);
                    int studentId = scan.nextInt();
                    print(COURSE_ID_TO_REMOVE_STUDENT);
                    int courseId = scan.nextInt();
                    facade.removeStudentFromCourse(studentId, courseId);
                }
                if (choice == 0) {
                    break;
                }
            }
        }
    }

    private static void print(String string) {
        System.out.println(string);
    }

    private static void printNewLine() {
        print("\n");
    }

    private static void getCourseList() throws ServiceException {
        List<Course> courseList;
        courseList = facade.getListOfCourses();
        for (int i = 0; i < courseList.size(); i++) {
            print("ID: " + courseList.get(i).getId() + ", Course name: " + courseList.get(i).getName()
                    + ", Course Description: " + courseList.get(i).getDescription());
        }
    }

    private static void getStudentCourseList(int courseId, int studentId) throws ServiceException {
        List<Course> studentsCourseList;
        studentsCourseList = facade.getCourseListByStudentId(courseId, studentId);

        for (Course course : studentsCourseList) {
            print("Student with ID " + studentId + " is applied to the course: " + course.getName());
        }
    }
}
