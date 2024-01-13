package Exceptions;

public class WrongPassword extends Exception {
    public WrongPassword(String name) {
        super("Zadali ste nesprávne heslo pre použivateľa " + name);
    }
}