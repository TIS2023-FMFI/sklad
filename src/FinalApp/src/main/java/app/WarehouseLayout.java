package app;

import Entity.Position;

import java.util.List;
import java.util.Map;

public class WarehouseLayout {
    private Map<Integer, List<Position>> shelfAndItsPositions;

    public WarehouseLayout() {
    }

    public void setShelfAndItsPositions(Map<Integer, List<Position>> map){
        shelfAndItsPositions = map;
    }

    public Map<Integer, List<Position>> getShelfAndItsPositions(){
        return shelfAndItsPositions;
    }
}
