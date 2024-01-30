package app;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DateTest {
    @Test
    void overlapDates(){
        CheckPositions checkPositions = new CheckPositions();
        Date from = new Date(2024, 1, 13);
        Date to = new Date(2024, 1, 15);
        assertFalse(checkPositions.overlapDate(from, to, new Date(2024, 1, 12)));
        assertTrue(checkPositions.overlapDate(from, to, new Date(2024, 1, 13)));
        assertTrue(checkPositions.overlapDate(from, to, new Date(2024, 1, 14)));
        assertTrue(checkPositions.overlapDate(from, to, new Date(2024, 1, 15)));
        assertFalse(checkPositions.overlapDate(from, to, new Date(2024, 1, 16)));
        assertFalse(checkPositions.overlapDate(from, to, new Date(2024, 1, 17)));
    }
}
