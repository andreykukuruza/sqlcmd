package controller.command.exception;

public class inputDataIncorrectException extends RuntimeException {
    public inputDataIncorrectException(String message, Exception cause) {
        super(message, cause);
    }

    public inputDataIncorrectException(String message) {
        super(message);
    }

    public inputDataIncorrectException() {
        super();
    }
}
