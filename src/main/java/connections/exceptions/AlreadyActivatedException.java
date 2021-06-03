package connections.exceptions;

public class AlreadyActivatedException extends RuntimeException {
    public AlreadyActivatedException(String errorMessage) {
        super(errorMessage);
    }
}
