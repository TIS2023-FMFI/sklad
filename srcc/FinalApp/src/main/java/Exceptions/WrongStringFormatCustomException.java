package Exceptions;

public class WrongStringFormatCustomException extends Exception {
    public WrongStringFormatCustomException(String m) {
        super("Nesprány formát: " + m);
    }
}