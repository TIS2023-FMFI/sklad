package Exceptions;

public class WrongStringFormat extends Exception {
    public WrongStringFormat(String m) {
        super("Nesprávny formát: " + m);
    }
}