package ua.foxminded.university.reader;

public class ReaderException extends Exception {
    private static final long serialVersionUID = 1L;

    public ReaderException(String errorMessage) {
        super(errorMessage);
    }

    public ReaderException(String errorMessage, Throwable error) {
        super(errorMessage, error);
    }

}
