package main.browse;

public class CannotAddRepeatException extends RuntimeException {
    // See: https://www.baeldung.com/java-new-custom-exception
    public CannotAddRepeatException(String errorMessage) {
        super(errorMessage);
    }
}
