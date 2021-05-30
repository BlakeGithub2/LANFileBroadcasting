package main.exceptions;

public class InvalidKeyCodeException extends RuntimeException {
    public InvalidKeyCodeException(String errorMessage) {
        super(errorMessage);
    }
}
