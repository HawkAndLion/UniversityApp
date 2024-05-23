package ua.foxminded.university.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.NoArgsConstructor;
import ua.foxminded.university.dao.CourseDAO;
import ua.foxminded.university.dao.CourseDAOImpl;
import ua.foxminded.university.dao.DaoException;
import ua.foxminded.university.dao.StudentDAO;
import ua.foxminded.university.dao.StudentDAOImpl;
import ua.foxminded.university.data.Course;
import ua.foxminded.university.data.Student;
import ua.foxminded.university.generator.StudentGenerator;
import ua.foxminded.university.reader.Reader;
import ua.foxminded.university.reader.ReaderException;

@NoArgsConstructor
public class StudentService {
    private static final String CHECK_QUERY = "Check the query or duplicate key value, or Database access ";
    private static final String NULL_ERROR_MESSAGE = "Argument cannot be null ";
    private static final String ERROR_MESSAGE = "Please check pathname or access rights ";
    private Reader reader = new Reader();
    private StudentGenerator generator = new StudentGenerator();
    private StudentDAO studentDao = new StudentDAOImpl();
    private CourseDAO courseDao = new CourseDAOImpl();

    public StudentService(StudentDAO studentDao, CourseDAO courseDao, StudentGenerator generator, Reader reader) {
        this.studentDao = studentDao;
        this.courseDao = courseDao;
        this.generator = generator;
        this.reader = reader;
    }

    public List<Student> getUpdatedListOfStudents() throws ServiceException {
        try {
            return studentDao.getAll();
        } catch (DaoException exception) {
            throw new ServiceException(CHECK_QUERY, exception);
        }
    }

    public List<Student> addNewStudentToList(String firstName, String lastName) throws ServiceException {
        try {
            studentDao.insert(new Student(firstName, lastName));
            return studentDao.getAll();
        } catch (DaoException exception) {
            throw new ServiceException(CHECK_QUERY, exception);
        }
    }

    public List<Student> removeStudentFromList(int studentId) throws ServiceException {
        try {
            Student student = studentDao.getById(studentId);
            List<Course> courseListByStudent;
            courseListByStudent = courseDao.findCoursesByStudentId(studentId);

            for (Course course : courseListByStudent) {
                studentDao.removeStudentFromCourse(student, course);
            }
            return studentDao.getAll();
        } catch (DaoException exception) {
            throw new ServiceException(CHECK_QUERY, exception);
        }
    }

    public List<Student> getListOfStudents(String fileWithFirstNames, String fileWithLastNames,
            int totalNumberOfStudents) throws ServiceException {
        if (fileWithFirstNames == null || fileWithLastNames == null) {
            throw new IllegalArgumentException(NULL_ERROR_MESSAGE);
        }

        List<String> firstNamesList;
        List<String> lastNamesList;
        List<Student> studentList = new ArrayList<>();
        List<Student> uniqueListOfStudents = new ArrayList<>();

        try {
            firstNamesList = reader.readFile(fileWithFirstNames);
            lastNamesList = reader.readFile(fileWithLastNames);
        } catch (ReaderException readerExcepion) {
            throw new ServiceException(ERROR_MESSAGE, readerExcepion);
        }

        generator = new StudentGenerator(firstNamesList, lastNamesList);
        while (uniqueListOfStudents.size() != totalNumberOfStudents) {
            Student student = generator.generateStudent();
            studentList.add(student);
            uniqueListOfStudents = studentList.stream().distinct().collect(Collectors.toList());
        }

        try {
            for (int i = 0; i < uniqueListOfStudents.size(); i++) {
                studentDao.insert(uniqueListOfStudents.get(i));
            }
            studentList = studentDao.getAll();

        } catch (DaoException exception) {
            throw new ServiceException(CHECK_QUERY, exception);
        }
        return studentList;
    }
}
