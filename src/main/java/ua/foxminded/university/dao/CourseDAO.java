package ua.foxminded.university.dao;

import java.util.List;

import ua.foxminded.university.data.Course;

public interface CourseDAO extends GenericDao<Course> {
    Course getById(int id) throws DaoException;

    Course findByName(String name) throws DaoException;

    List<Course> findCoursesByStudentId(int studentId) throws DaoException;

    void removeStudentFromCourse(int studentId, int courseId) throws DaoException;
}
