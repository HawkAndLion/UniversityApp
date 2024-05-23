package ua.foxminded.university.dao;

import ua.foxminded.university.data.Group;

public interface GroupDAO extends GenericDao<Group> {
    Group getById(int id) throws DaoException;

    Group findByName(String name) throws DaoException;

}
