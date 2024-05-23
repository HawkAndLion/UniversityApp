package ua.foxminded.university.parser;

import java.util.List;
import java.util.stream.Collectors;

import ua.foxminded.university.data.Course;

public class CourseParser {
    private static final String COMMA = ",";
    private static final String NULL_ERROR_MESSAGE = "Argument cannot be null ";

    public List<Course> extractListOfCourses(List<String> coursesFromFile) {
        if (coursesFromFile == null) {
            throw new IllegalArgumentException(NULL_ERROR_MESSAGE);
        }

        List<Course> listOfCourses;
        listOfCourses = coursesFromFile.stream().map(line -> line.split(COMMA))
                .map(line -> new Course(line[0], line[1])).collect(Collectors.toList());

        return listOfCourses;
    }
}
