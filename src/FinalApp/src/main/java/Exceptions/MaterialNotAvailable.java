package Exceptions;

public class MaterialNotAvailable extends Exception{
    public MaterialNotAvailable(String materialName) {
        super("Materiál " + materialName + " sa nenašiel.");
    }
}
