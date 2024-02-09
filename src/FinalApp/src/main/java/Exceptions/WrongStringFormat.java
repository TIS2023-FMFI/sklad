package Exceptions;

public class WrongStringFormat extends Exception {
    /**
     * Constructs a new WrongStringFormat exception with the specified message.
     *
     * @param message The message indicating the incorrect format.
     */
    public WrongStringFormat(String message) {
        super("Nesprávny formát: " + message);
    }
}