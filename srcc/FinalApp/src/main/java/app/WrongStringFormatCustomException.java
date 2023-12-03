package app;

public class WrongStringFormatCustomException extends Exception {
    public WrongStringFormatCustomException(String m) {
        super(m);
    }
}