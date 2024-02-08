package Exceptions;

public class WrongPassword extends Exception {
    /**
     * Constructs a new WrongPassword exception with the specified username.
     *
     * @param name The username for which the wrong password was provided.
     */
    public WrongPassword(String name) {
        super("Zadali ste nesprávne heslo pre použivateľa " + name);
    }
}