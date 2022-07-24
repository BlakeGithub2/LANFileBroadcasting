package connections.exceptions;

public class NoProjectExistsException extends RuntimeException {
    public NoProjectExistsException(String errorMessage) {
        super(errorMessage);
    }
}
