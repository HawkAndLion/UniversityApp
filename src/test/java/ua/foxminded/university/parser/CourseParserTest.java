package ua.foxminded.university.parser;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import ua.foxminded.university.data.Course;
import ua.foxminded.university.reader.ReaderException;

class CourseParserTest {
    private static final String ALGEBRA = "Algebra";
    private static final String ALGEBRA_DESCRIPTION = " Develops algebraic concepts and skills.";
    private static final String ALGEBRA_AND_DESCRIPTION = "Algebra, Develops algebraic concepts and skills.";
    private static final String NULL_ERROR_MESSAGE = "Argument cannot be null ";
    private CourseParser parser = new CourseParser();

    @Test
    void shouldReturnSameListWhenVerified() throws ReaderException {
        List<String> textFromFile = new ArrayList<>();
        textFromFile.add(ALGEBRA_AND_DESCRIPTION);
        List<Course> expected = new ArrayList<>();
        expected.add(new Course(ALGEBRA, ALGEBRA_DESCRIPTION));
        List<Course> actual = new ArrayList<>();
        actual = parser.extractListOfCourses(textFromFile);

        assertEquals(expected, actual);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenNullIsPassed() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            parser.extractListOfCourses(null);
        });

        assertEquals(NULL_ERROR_MESSAGE, exception.getMessage());
    }
}
