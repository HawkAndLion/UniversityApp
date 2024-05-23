package ua.foxminded.university.dao;

import java.util.List;

import ua.foxminded.university.data.Course;
import ua.foxminded.university.data.Student;

public interface StudentDAO extends GenericDao<Student> {
    Student getById(int id) throws DaoException;

    Student findByLastName(String name) throws DaoException;

    void insertCourse(Student student, Course course) throws DaoException;

    Student removeStudentFromCourse(Student student, Course course) throws DaoException;

    List<Student> getStudentListByCourseName(String courseName) throws DaoException;
}
