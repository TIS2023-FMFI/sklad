package Exceptions;

public class EmptyPassword extends Exception {
    public EmptyPassword() {
        super("Nezadali ste používateľské heslo");
    }
}