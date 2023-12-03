package Exceptions;

public class FileNotFound extends Exception{
    public FileNotFound(String fileName) {
        super("Súbor " + fileName + " neexistuje");
    }
}
