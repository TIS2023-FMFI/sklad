package Exceptions;

public class FileNotFound extends Exception{
    /**
     * Constructs a new FileNotFound exception with the specified file name.
     *
     * @param fileName The name of the file that was not found.
     */
    public FileNotFound(String fileName) {
        super("Súbor " + fileName + " sa nenašiel.");
    }
}
