package ua.foxminded.university.service;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import lombok.NoArgsConstructor;
import ua.foxminded.university.dao.CourseDAO;
import ua.foxminded.university.dao.CourseDAOImpl;
import ua.foxminded.university.dao.DaoException;
import ua.foxminded.university.dao.StudentDAO;
import ua.foxminded.university.dao.StudentDAOImpl;
import ua.foxminded.university.data.Course;
import ua.foxminded.university.data.Student;
import ua.foxminded.university.parser.CourseParser;
import ua.foxminded.university.reader.Reader;
import ua.foxminded.university.reader.ReaderException;

@NoArgsConstructor
public class CourseService {
    private static final String ERROR_NULL_MESSAGE = "Argument cannot be null or empty ";
    private static final String CHECK_QUERY = "Check the query or duplicate key value, or Database access ";
    private static final String ERROR_MESSAGE = "Please check pathname or access rights ";
    private Random random = new Random();
    private StudentDAO studentDao = new StudentDAOImpl();
    private CourseDAO courseDao = new CourseDAOImpl();
    private Reader reader = new Reader();
    private CourseParser parser = new CourseParser();

    public CourseService(Random random, StudentDAO studentDao, CourseDAO courseDao, Reader reader,
            CourseParser parser) {
        this.random = random;
        this.studentDao = studentDao;
        this.courseDao = courseDao;
        this.reader = reader;
        this.parser = parser;
    }

    public void assignCoursesToStudent(List<Student> listOfStudents, List<Course> listOfCourses,
            int minCoursesPerStudent, int maxCoursesPerStudent, int minNumberOfCourses, int maxNumberOfCourses)
            throws ServiceException {
        if (listOfStudents == null || listOfStudents.isEmpty() || listOfCourses == null || listOfCourses.isEmpty()) {
            throw new IllegalArgumentException(ERROR_NULL_MESSAGE);
        }

        for (Student student : listOfStudents) {
            Set<Integer> objects = new LinkedHashSet<>();
            int courseAmountPerStudent = random.nextInt(maxCoursesPerStudent - minCoursesPerStudent)
                    + minCoursesPerStudent;
            int courseCounter = objects.size();

            while (courseCounter != courseAmountPerStudent) {
                int randomIndexOfCourses = random.nextInt(maxNumberOfCourses - minNumberOfCourses) + minNumberOfCourses;
                objects.add(randomIndexOfCourses);
                courseCounter = objects.size();
            }

            Iterator<Integer> iterator = objects.iterator();
            try {
                while (iterator.hasNext()) {
                    studentDao.insertCourse(student, listOfCourses.get(iterator.next()));
                }
            } catch (DaoException exception) {
                throw new ServiceException(CHECK_QUERY, exception);
            }
        }
    }

    public void insertCourseByStudentId(int courseId, int studentId) throws ServiceException {
        try {
            Course course = courseDao.getById(courseId);
            Student student = studentDao.getById(studentId);
            studentDao.insertCourse(student, course);
        } catch (DaoException exception) {
            throw new ServiceException(CHECK_QUERY, exception);
        }
    }

    public List<Course> getCourseListByStudentId(int studentId) throws ServiceException {
        try {
            return courseDao.findCoursesByStudentId(studentId);
        } catch (DaoException exception) {
            throw new ServiceException(CHECK_QUERY, exception);
        }
    }

    public void removeStudentFromCourseList(int studentId, int courseId) throws ServiceException {
        try {
            courseDao.removeStudentFromCourse(studentId, courseId);
        } catch (DaoException exception) {
            throw new ServiceException(CHECK_QUERY, exception);
        }
    }

    public List<Student> getStudentListByCourseName(String courseName) throws ServiceException {
        try {
            return studentDao.getStudentListByCourseName(courseName);
        } catch (DaoException exception) {
            throw new ServiceException(CHECK_QUERY, exception);
        }
    }

    public List<Course> getListOfCoursesFromDatabase() throws ServiceException {
        try {
            return courseDao.getAll();
        } catch (DaoException exception) {
            throw new ServiceException(CHECK_QUERY, exception);
        }
    }

    public List<Course> getListOfCourses(String fileName) throws ServiceException {
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException(ERROR_NULL_MESSAGE);
        }

        try {
            List<String> textFromFile;
            textFromFile = reader.readFile(fileName);
            List<Course> listOfCoursesFromFile;
            listOfCoursesFromFile = parser.extractListOfCourses(textFromFile);

            for (int i = 0; i < listOfCoursesFromFile.size(); i++) {
                courseDao.insert(new Course(listOfCoursesFromFile.get(i).getName(),
                        listOfCoursesFromFile.get(i).getDescription()));
            }
            return courseDao.getAll();
        } catch (ReaderException readerExcepion) {
            throw new ServiceException(ERROR_MESSAGE, readerExcepion);
        } catch (DaoException exception) {
            throw new ServiceException(CHECK_QUERY, exception);
        }
    }

    public Course getCourseById(int id) throws ServiceException {
        try {
            return courseDao.getById(id);
        } catch (DaoException exception) {
            throw new ServiceException(CHECK_QUERY, exception);
        }
    }
}
