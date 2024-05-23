package ua.foxminded.university.reader;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReaderTest {
    private static final String FIRST_NAMES = "firstNamesTest.txt";
    private static final String LAST_NAMES = "lastNamesTest.txt";
    private static final String MAJOR_AND_DESCRIPTION = "majorAndDescriptionTest.txt";
    private static final String ERROR_TEXT = "Argument cannot be null or empty ";
    private Reader reader;

    @BeforeEach
    void setUp() {
        reader = new Reader();
    }

    @Test
    void shouldReturnSameDataWhenFirstNamesFileIsRead() throws ReaderException {
        List<String> expected = new ArrayList<>();
        expected.add("Maria");
        expected.add("Jacob");
        expected.add("Ben");
        expected.add("Nancy");
        expected.add("Andrew");

        List<String> actual = new ArrayList<>();
        actual = reader.readFile(FIRST_NAMES);

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnSameDataWhenLastNamesFileIsRead() throws ReaderException {
        List<String> expected = new ArrayList<>();
        expected.add("Timberlake");
        expected.add("Brown");
        expected.add("Berrimore");
        expected.add("Shaw");
        expected.add("McKenzey");

        List<String> actual = new ArrayList<>();
        actual = reader.readFile(LAST_NAMES);

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnSameDataWhenMajorFileIsRead() throws ReaderException {
        List<String> expected = new ArrayList<>();
        expected.add("Algebra, Develops algebraic concepts and skills.");
        expected.add("Geometry, Presents the study of several geometries.");
        expected.add("Geography, An introduction to the earth’s natural environmental systems.");

        List<String> actual = new ArrayList<>();
        actual = reader.readFile(MAJOR_AND_DESCRIPTION);

        assertEquals(expected, actual);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenNullIsPassed() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reader.readFile(null);
        });

        assertEquals(ERROR_TEXT, exception.getMessage());
    }
}
