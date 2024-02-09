package Exceptions;

public class UserDoesNotExist extends Exception {
    /**
     * Constructs a new UserDoesNotExist exception with the specified username.
     *
     * @param name The name of the user that does not exist.
     */
    public UserDoesNotExist(String name) {
        super("Používateľ s menom " + name + " neexistuje");
    }
}
