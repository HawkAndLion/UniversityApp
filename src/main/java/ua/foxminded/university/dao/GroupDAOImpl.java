package ua.foxminded.university.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ua.foxminded.university.data.Group;
import ua.foxminded.university.domain.ConnectionManager;

public class GroupDAOImpl implements GroupDAO {
    private static final String SQL_INSERT_GROUP_NAME = "INSERT INTO university.GROUPS (GROUP_NAME) VALUES (?)";
    private static final String SQL_GET_ALL_GROUPS = "SELECT GROUP_ID, GROUP_NAME FROM university.GROUPS";
    private static final String SQL_UPDATE_GROUP = "UPDATE university.GROUPS SET GROUP_NAME=? WHERE GROUP_ID=?";
    private static final String SQL_DELETE_GROUP = "DELETE FROM university.GROUPS WHERE GROUP_ID=?";
    private static final String SQL_GET_BY_ID = "SELECT GROUP_ID, GROUP_NAME FROM university.GROUPS WHERE GROUP_ID=?";
    private static final String SQL_GET_BY_NAME = "SELECT GROUP_ID, GROUP_NAME FROM university.GROUPS WHERE GROUP_NAME=?";
    private static final String GROUP_ID = "GROUP_ID";
    private static final String GROUP_NAME = "GROUP_NAME";
    private static final String CHECK_QUERY = "Check the query or duplicate key value, or Database access ";
    private static final String ERROR_MESSAGE = "Argument cannot be null ";
    private static final String ERROR_TEXT = "Check the argument. It might be empty ";

    @Override
    public Group insert(Group group) throws DaoException {
        if (group == null) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }

        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_INSERT_GROUP_NAME);) {
            statement.setString(1, group.getName());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new DaoException(CHECK_QUERY, exception);
        }
        return group;
    }

    @Override
    public List<Group> getAll() throws DaoException {
        List<Group> listOfGroups = new ArrayList<>();

        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_GET_ALL_GROUPS);
                ResultSet resultSet = statement.executeQuery();) {

            while (resultSet.next()) {
                int groupId = resultSet.getInt(GROUP_ID);
                String groupName = resultSet.getString(GROUP_NAME);

                Group group = new Group(groupId, groupName);

                listOfGroups.add(group);
            }
        } catch (SQLException exception) {
            throw new DaoException(CHECK_QUERY, exception);
        }
        return listOfGroups;
    }

    @Override
    public Group update(Group group) throws DaoException {
        if (group == null) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }

        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_GROUP)) {
            statement.setString(1, group.getName());
            statement.setInt(2, group.getId());

            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new DaoException(CHECK_QUERY, exception);
        }
        return group;
    }

    @Override
    public Group remove(Group group) throws DaoException {
        if (group == null) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }

        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_DELETE_GROUP);) {
            statement.setInt(1, group.getId());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new DaoException(CHECK_QUERY, exception);
        }
        return group;
    }

    @Override
    public Group getById(int id) throws DaoException {
        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_GET_BY_ID);) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery();) {
                if (resultSet.next()) {
                    int groupId = resultSet.getInt(GROUP_ID);
                    String groupName = resultSet.getString(GROUP_NAME);
                    return new Group(groupId, groupName);
                } else {
                    throw new DaoException(ERROR_TEXT);
                }
            }
        } catch (SQLException exception) {
            throw new DaoException(CHECK_QUERY, exception);
        }
    }

    @Override
    public Group findByName(String name) throws DaoException {
        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_GET_BY_NAME)) {

            statement.setString(1, name);

            try (ResultSet resultSet = statement.executeQuery();) {
                if (resultSet.next()) {
                    int groupId = resultSet.getInt(GROUP_ID);
                    String groupName = resultSet.getString(GROUP_NAME);
                    return new Group(groupId, groupName);
                } else {
                    throw new DaoException(ERROR_TEXT);
                }
            }
        } catch (SQLException exception) {
            throw new DaoException(CHECK_QUERY, exception);
        }
    }
}
