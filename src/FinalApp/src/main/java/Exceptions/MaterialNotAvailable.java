package Exceptions;

public class MaterialNotAvailable extends Exception{
    /**
     * Constructs a new MaterialNotAvailable exception with the specified material name.
     *
     * @param materialName The name of the material that is not available.
     */
    public MaterialNotAvailable(String materialName) {
        super("Materiál " + materialName + " sa nenašiel.");
    }
}
