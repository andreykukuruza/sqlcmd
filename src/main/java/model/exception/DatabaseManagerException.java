package model.exception;

public class DatabaseManagerException extends RuntimeException {
    public DatabaseManagerException(String message, Exception cause) {
        super(message, cause);
    }

    public DatabaseManagerException(String message) {
        super(message);
    }

    public DatabaseManagerException() {
        super();
    }
}
