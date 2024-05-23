package ua.foxminded.university.reader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import lombok.extern.java.Log;
import ua.foxminded.university.domain.ConnectionManager;

@Log
public class Reader {
    private static final String ERROR_TEXT = "Argument cannot be null or empty ";
    private static final String ERROR_MESSAGE = "Please check pathname or access rights ";
    private static final String ERROR_CLASSPATH_MESSAGE = "ERROR: File is not found in the classpath!";
    private static final String ERROR_CHECK_MESSAGE = "ERROR: check access to file or check its contents";

    public List<String> readFile(String file) throws ReaderException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(ERROR_TEXT);
        }

        List<String> textFromFile = new ArrayList<>();

        try (InputStream inputStream = Reader.class.getClassLoader().getResourceAsStream(file)) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
                textFromFile = br.lines().collect(Collectors.toList());
            }
        } catch (IOException error) {
            throw new ReaderException(ERROR_MESSAGE, error);
        }
        return textFromFile;
    }

    public Properties getPropertiesFromConfigFile(String configFile) {
        Properties property = new Properties();
        try (InputStream inputStream = ConnectionManager.class.getClassLoader().getResourceAsStream(configFile);) {
            property.load(inputStream);
        } catch (FileNotFoundException e) {
            log.info(ERROR_CLASSPATH_MESSAGE);
        } catch (IOException e) {
            log.info(ERROR_CHECK_MESSAGE);
        }
        return property;
    }
}
