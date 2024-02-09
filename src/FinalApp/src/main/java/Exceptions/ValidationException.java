package Exceptions;

public class ValidationException extends Exception {
    /**
     * Constructs a new ValidationException with the specified error message.
     *
     * @param message The error message describing the validation issue.
     */
    public ValidationException(String message) {
        super(message);
    }
}