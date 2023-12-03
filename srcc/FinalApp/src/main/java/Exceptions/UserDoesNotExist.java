package Exceptions;

public class UserDoesNotExist extends Exception {
    public UserDoesNotExist(String name) {
        super("Používateľ s menom " + name + " neexistuje");
    }
}
