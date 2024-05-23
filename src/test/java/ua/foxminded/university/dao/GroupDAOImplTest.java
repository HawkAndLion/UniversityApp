package ua.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ua.foxminded.university.data.Group;

class GroupDAOImplTest {
    private static final String ERROR_MESSAGE = "Argument cannot be null ";
    private static final String GROUP_KZ = "KZ-55";
    private static final String GROUP_RA = "RA-23";
    private static final String GROUP_NL = "NL-77";
    private static final String GROUP_YU = "YU-10";
    private GroupDAO groupDAO;

    @BeforeEach
    void setUp() {
        groupDAO = new GroupDAOImpl();
    }

    @Test
    void shouldReturnSameGroupWhenInserted() throws DaoException {
        Group expectedGroup = new Group(1, GROUP_KZ);
        Group actualGroup = groupDAO.insert(expectedGroup);

        assertEquals(expectedGroup, actualGroup);
    }

    @Test
    void shouldReturnSameSizeWhenMethodExecuted() throws DaoException {
        List<Group> actual = new ArrayList<>();
        actual = groupDAO.getAll();
        assertNotNull(actual);
        if (actual.size() == 1) {
            assertEquals(1, actual.size());
        } else {
            assertEquals(2, actual.size());
        }
    }

    @Test
    void shouldReturnSameGroupWhenGroupUpdated() throws DaoException {
        Group expectedGroup = new Group(7, GROUP_RA);
        Group actualGroup = groupDAO.update(expectedGroup);
        assertNotNull(expectedGroup);
        assertEquals(expectedGroup, actualGroup);

        Group returnGroup = new Group(7, GROUP_NL);
        groupDAO.update(returnGroup);
    }

    @Test
    void shouldReturnSameListWhenGroupRemoved() throws DaoException {
        Group newGroup = new Group(2, GROUP_YU);
        groupDAO.insert(newGroup);
        assertNotNull(newGroup);
        Group group = groupDAO.findByName(newGroup.getName());
        groupDAO.remove(group);
        List<Group> actual = new ArrayList<>();
        actual = groupDAO.getAll();
        assertNotNull(actual);

        List<Group> expected = new ArrayList<>();
        if (actual.size() == 2) {
            expected.add(new Group(1, GROUP_KZ));
            expected.add(new Group(7, GROUP_NL));
        } else {
            expected.add(new Group(7, GROUP_NL));
        }
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnSameGroupWhenMethodIsExecuted() throws DaoException {
        Group expectedGroup = new Group(7, GROUP_NL);
        assertNotNull(expectedGroup);
        Group actualGroup = groupDAO.getById(expectedGroup.getId());
        assertNotNull(actualGroup);
        assertEquals(expectedGroup, actualGroup);
    }

    @Test
    void shouldReturnSameDataWhenMethodIsExecuted() throws DaoException {
        Group expectedGroup = new Group(7, GROUP_NL);
        Group actualGroup = groupDAO.findByName(expectedGroup.getName());
        assertEquals(expectedGroup, actualGroup);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenInsertedObjectIsNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            groupDAO.insert(null);
        });
        assertEquals(ERROR_MESSAGE, exception.getMessage());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenUpdatedObjectIsNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            groupDAO.update(null);
        });
        assertEquals(ERROR_MESSAGE, exception.getMessage());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenObjectIsNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            groupDAO.remove(null);
        });
        assertEquals(ERROR_MESSAGE, exception.getMessage());
    }
}
