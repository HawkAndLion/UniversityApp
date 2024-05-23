package ua.foxminded.university.dao;

import java.util.List;

public interface GenericDao<T> {
    T insert(T t) throws DaoException;

    List<T> getAll() throws DaoException;

    T update(T t) throws DaoException;

    T remove(T t) throws DaoException;
}
