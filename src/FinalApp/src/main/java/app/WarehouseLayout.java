package app;

import Entity.Position;

import java.util.List;
import java.util.Map;

public class WarehouseLayout {
    private Map<Integer, List<Position>> shelfAndItsPositions;

    /***
     * Constructor
     */
    public WarehouseLayout() {}

    /***
     * Set the shelf and its positions
     * @param map - Map of shelf and its positions
     */
    public void setShelfAndItsPositions(Map<Integer, List<Position>> map){
        shelfAndItsPositions = map;
    }

    /***
     * Get the shelf and its positions
     * @return - Map of shelf and its positions
     */
    public Map<Integer, List<Position>> getShelfAndItsPositions(){
        return shelfAndItsPositions;
    }
}
