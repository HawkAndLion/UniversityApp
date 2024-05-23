package ua.foxminded.university.generator;

import java.util.List;
import java.util.Random;

import lombok.NoArgsConstructor;
import ua.foxminded.university.data.Student;

@NoArgsConstructor
public class StudentGenerator {
    private List<String> firstNamesList;
    private List<String> lastNamesList;
    private Random random = new Random();

    public StudentGenerator(List<String> firstNamesList, List<String> lastNamesList) {
        this.firstNamesList = firstNamesList;
        this.lastNamesList = lastNamesList;
    }

    public Student generateStudent() {
        return new Student(generateFirstName(firstNamesList), generateLastName(lastNamesList));
    }

    private String generateFirstName(List<String> firstNamesList) {
        int indexForFirstNames = random.nextInt(firstNamesList.size());
        return firstNamesList.get(indexForFirstNames);
    }

    private String generateLastName(List<String> lastNamesList) {
        int indexForLastNames = random.nextInt(lastNamesList.size());
        return lastNamesList.get(indexForLastNames);
    }
}
