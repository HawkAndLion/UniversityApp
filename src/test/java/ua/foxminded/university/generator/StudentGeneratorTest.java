package ua.foxminded.university.generator;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ua.foxminded.university.data.Student;

class StudentGeneratorTest {
    private List<String> firstNamesList = new ArrayList<>();
    private List<String> lastNamesList = new ArrayList<>();
    private StudentGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new StudentGenerator(firstNamesList, lastNamesList);
    }

    @Test
    void shouldReturnSameDataWhenGenerateStudent() {
        firstNamesList.add("Charles");
        lastNamesList.add("Xavier");
        Student expectedStudent = new Student("Charles", "Xavier");
        Student actualStudent = generator.generateStudent();

        assertEquals(expectedStudent, actualStudent);
    }
}
