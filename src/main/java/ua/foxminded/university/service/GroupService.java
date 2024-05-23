package ua.foxminded.university.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.NoArgsConstructor;
import ua.foxminded.university.dao.DaoException;
import ua.foxminded.university.dao.GroupDAO;
import ua.foxminded.university.dao.GroupDAOImpl;
import ua.foxminded.university.dao.StudentDAO;
import ua.foxminded.university.dao.StudentDAOImpl;
import ua.foxminded.university.data.Group;
import ua.foxminded.university.data.Student;
import ua.foxminded.university.generator.GroupGenerator;

@NoArgsConstructor
public class GroupService {
    private static final String ERROR_MESSAGE = "Argument cannot be null or empty ";
    private static final String CHECK_QUERY = "Check the query or duplicate key value, or Database access ";
    private static final int STUDENTS_WITH_NO_GROUP = 0;
    private static final String NO_GROUP_NAME = "No name";
    private Random random = new Random();
    private StudentDAO studentDao = new StudentDAOImpl();
    private GroupDAO groupDao = new GroupDAOImpl();
    private GroupGenerator generator = new GroupGenerator();

    public GroupService(Random random, StudentDAO studentDao, GroupDAO groupDao, GroupGenerator generator) {
        super();
        this.random = random;
        this.studentDao = studentDao;
        this.groupDao = groupDao;
        this.generator = generator;
    }

    public List<Student> assignStudentsToGroups(List<Student> listOfStudents, List<Group> listOfGroups,
            int minNumberOfStudents, int maxNumberOfStudents) throws ServiceException {
        if (listOfStudents == null || listOfStudents.isEmpty() || listOfGroups == null || listOfGroups.isEmpty()) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }
        Student student;
        int numberOfStudents = 0;
        int counterOfStudents = 0;
        int totalNumberOfStudents = 0;

        try {
            for (Group group : listOfGroups) {
                int randomStudentAmount = random.nextInt(maxNumberOfStudents - minNumberOfStudents)
                        + minNumberOfStudents;
                totalNumberOfStudents += randomStudentAmount;

                if (listOfStudents.size() - totalNumberOfStudents >= minNumberOfStudents) {
                    for (int i = 0; i < randomStudentAmount; i++) {
                        if (numberOfStudents + i < listOfStudents.size()) {
                            listOfStudents.get(numberOfStudents + i).setGroupId(group.getId());

                            student = new Student(listOfStudents.get(numberOfStudents + i).getStudentId(),
                                    listOfStudents.get(numberOfStudents + i).getGroupId(),
                                    listOfStudents.get(numberOfStudents + i).getFirstName(),
                                    listOfStudents.get(numberOfStudents + i).getLastName());

                            studentDao.update(student);
                            counterOfStudents++;
                        }
                    }
                    numberOfStudents = counterOfStudents;
                }
            }
            listOfStudents = studentDao.getAll();
        } catch (DaoException exception) {
            throw new ServiceException(CHECK_QUERY, exception);
        }
        return listOfStudents;
    }

    public List<Group> findGroups(int inputNumber, List<Student> listOfStudents, List<Group> listOfGroups) {
        if (listOfStudents == null || listOfGroups == null || listOfStudents.isEmpty() || listOfGroups.isEmpty()) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }

        List<Group> groupList = new ArrayList<>();
        int numberOfStudents = 0;
        for (Group group : listOfGroups) {
            numberOfStudents = getNumberOfStudentsInGroup(listOfStudents, group.getId());
            if (numberOfStudents <= inputNumber) {
                groupList.add(group);
            }
        }

        int noGroup = getNumberOfStudentsInGroup(listOfStudents, STUDENTS_WITH_NO_GROUP);
        if (noGroup <= inputNumber) {
            groupList.add(new Group(STUDENTS_WITH_NO_GROUP, NO_GROUP_NAME));
        }
        return groupList;
    }

    public List<Group> getListOfGroups(int numberOfGroups) throws ServiceException {
        List<Group> listOfGroups = new ArrayList<>();
        Set<Group> objects = new LinkedHashSet<>();
        int counter = 0;

        while (counter != numberOfGroups) {
            Group newGroup = generator.getGroup();
            objects.add(newGroup);
            counter = objects.size();
        }

        Iterator<Group> iterator = objects.iterator();
        while (iterator.hasNext()) {
            listOfGroups.add(iterator.next());
        }

        try {
            for (Group group : listOfGroups) {
                groupDao.insert(group);
            }
            listOfGroups = groupDao.getAll();
        } catch (DaoException exception) {
            throw new ServiceException(CHECK_QUERY, exception);
        }
        return listOfGroups;
    }

    private int getNumberOfStudentsInGroup(List<Student> listOfStudents, int groupId) {
        AtomicInteger counter = new AtomicInteger();
        for (Student student : listOfStudents) {
            if (groupId == student.getGroupId()) {
                counter.getAndIncrement();
            }
        }
        return counter.intValue();
    }
}
