package app;

import Entity.Position;
import Exceptions.FileNotFound;
import Exceptions.WrongStringFormat;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LoadPositionsTest {
    private static final String FILE_NAME = "warehouse_layout.txt";
    private  final String TEST_FILE_NAME = "loadPositionExample.txt";
    @Test
    void checkNameShort() throws  FileNotFound {
        LoadPositions load = new LoadPositions(TEST_FILE_NAME);
        assertFalse(load.checkName("A105"));
    }

    @Test
    void uploadTestDate() throws FileNotFound, WrongStringFormat {
        LoadPositions load = new LoadPositions(TEST_FILE_NAME);
        System.out.println(load.addPositions());
        assertTrue(load.saveToDB());
    }

    @Test
    void checkNameWrongFirstLetter() throws  FileNotFound {
        LoadPositions load = new LoadPositions(TEST_FILE_NAME);
        assertFalse(load.checkName("G010A"));
    }

    @Test
    void checkNameLetterInSecondPart() throws  FileNotFound {
        LoadPositions load = new LoadPositions(TEST_FILE_NAME);
        assertFalse(load.checkName("A010A"));
    }

    @Test
    void checkNameCorrect() throws  FileNotFound {
        LoadPositions load = new LoadPositions(TEST_FILE_NAME);
        assertTrue(load.checkName("A0103"));
    }

    @Test
    void savePosition() throws FileNotFound {
        LoadPositions load = new LoadPositions(TEST_FILE_NAME);
        String name = "A0103";
        assertTrue(load.checkName("A0103"));
        assertTrue(load.savePosition(name, true));
    }

    @Test
    void saveSamePosition() throws FileNotFound {
        LoadPositions load = new LoadPositions(TEST_FILE_NAME);
        String name = "A0103";
        assertTrue(load.checkName("A0103"));
        assertTrue(load.savePosition(name, true));
        assertFalse(load.savePosition(name, true));
    }

    @Test
    void addPositions_invalidInput_throwWrongStringFormatException() throws FileNotFound {
        LoadPositions load = new LoadPositions(TEST_FILE_NAME);
        load.rows.add("v-A101-B102-C103-D104-E105");
        assertThrows(WrongStringFormat.class, load::addPositions);
    }


    @Test
    void checkName_invalidLength_returnFalse() throws FileNotFound {
        LoadPositions load = new LoadPositions(TEST_FILE_NAME);
        assertFalse(load.checkName("A10"));
    }

    @Test

    void checkName_invalidFirstLetter_returnFalse() throws FileNotFound {
        LoadPositions load = new LoadPositions(TEST_FILE_NAME);
        assertFalse(load.checkName("G101"));
    }

    @Test
    void savePosition_validName_returnTrue() throws FileNotFound {
        LoadPositions load = new LoadPositions(TEST_FILE_NAME);
        assertTrue(load.savePosition("A1001", true));
        List<Position> finalPositions = load.finalPositions;
        assertEquals(1, finalPositions.size());
        assertEquals("A1001", finalPositions.get(0).getName());
    }
}
