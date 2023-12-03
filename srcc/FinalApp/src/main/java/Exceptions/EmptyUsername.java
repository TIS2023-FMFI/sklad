package Exceptions;

public class EmptyUsername extends Exception{
    public EmptyUsername() {
        super("Nezadali ste používateľské meno");
    }
}
