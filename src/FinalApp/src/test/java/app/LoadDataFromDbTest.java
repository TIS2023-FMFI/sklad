package app;

import Entity.Position;
import Exceptions.FileNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoadDataFromDbTest {
    private Map<String, List<Position>> warehouseData;

    @BeforeEach
    void setUp() throws Exception {
        DatabaseHandler db = new DatabaseHandler();
        warehouseData = db.getWarehouseData();
    }

    @Test
    void checkExistingRow(){
        System.out.println(warehouseData);
        assertTrue(warehouseData.containsKey("An"));
    }

    @Test
    void checkNotExistingRow(){
        assertFalse(warehouseData.containsKey("Pp"));
    }
}
