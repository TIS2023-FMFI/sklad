package app;

import Entity.Position;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class loadPositionsTest {

    @Test
    void addPositions_validInput_returnCorrectNumberOfPositions() throws WrongStringFormatCustomException, IOException {
        LoadPositions load = new LoadPositions();
        int addedPositions = load.addPositions();
        assertEquals(7, addedPositions); // Adjust the expected value based on your input file
    }

    @Test
    void addPositions_invalidInput_throwWrongStringFormatException() throws WrongStringFormatCustomException, IOException {
        LoadPositions load = new LoadPositions();
        load.rows.add("v-A101-B102-C103-D104-E105");
        assertThrows(WrongStringFormatCustomException.class, load::addPositions);
    }


    @Test
    void checkName_invalidLength_returnFalse() throws WrongStringFormatCustomException, IOException {
        LoadPositions load = new LoadPositions();
        assertFalse(load.checkName("A10"));
    }

    @Test
    void checkName_invalidFirstLetter_returnFalse() throws WrongStringFormatCustomException, IOException {
        LoadPositions load = new LoadPositions();
        assertFalse(load.checkName("G101"));
    }

    @Test
    void checkName_invalidNumberFormat_returnFalse() throws WrongStringFormatCustomException, IOException {
        LoadPositions load = new LoadPositions();
        assertFalse(load.checkName("A10A"));
    }

    @Test
    void savePosition_validName_returnTrue() throws WrongStringFormatCustomException, IOException {
        LoadPositions load = new LoadPositions();
        assertTrue(load.savePosition("A101", true));
        List<Position> finalPositions = load.finalPositions;
        assertEquals(1, finalPositions.size());
        assertEquals("A101", finalPositions.get(0).getName());
//        assertTrue(finalPositions.get(0).isTall());
    }

    @Test
    void savePosition_invalidName_throwWrongStringFormatException() throws WrongStringFormatCustomException, IOException {
        LoadPositions load = new LoadPositions();
        assertThrows(WrongStringFormatCustomException.class, () -> load.savePosition("G101", true));
    }
}
