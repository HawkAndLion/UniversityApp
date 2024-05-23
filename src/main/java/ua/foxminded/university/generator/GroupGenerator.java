package ua.foxminded.university.generator;

import java.util.Random;

import lombok.NoArgsConstructor;
import ua.foxminded.university.data.Group;

@NoArgsConstructor
public class GroupGenerator {
    private static final String HYPHEN = "-";
    private static final int MIN_NUMBER = 1;
    private static final int MAX_NUMBER = 9;
    private Random random = new Random();

    public Group getGroup() {
        return new Group(getRandomGroupName());
    }

    private int getRandomNumber() {
        return random.nextInt(MAX_NUMBER - MIN_NUMBER) + MIN_NUMBER;
    }

    private char getRandomChar() {
        return (char) (random.nextInt(26) + 'A');
    }

    private String getRandomGroupName() {
        StringBuilder builder = new StringBuilder();
        builder.append(getRandomChar()).append(getRandomChar()).append(HYPHEN).append(getRandomNumber())
                .append(getRandomNumber());

        return builder.toString();
    }
}
