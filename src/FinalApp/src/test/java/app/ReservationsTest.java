package app;

import Entity.Users;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class ReservationsTest {

    @Test
    void betweenDates(){
        Reservation reservation = new Reservation();
        Date from = new Date(2024, 1, 13);
        Date to = new Date(2024, 1, 15);
        reservation.countAllFreePositions(from, to);
        assertTrue(reservation.overlapDate(new Date(2024, 1, 13), (new Date(2024, 1, 14))));
        assertTrue(reservation.overlapDate(new Date(2024, 1, 13), (new Date(2024, 1, 15))));
        assertTrue(reservation.overlapDate(new Date(2024, 1, 13), (new Date(2024, 1, 16))));
        assertTrue(reservation.overlapDate(new Date(2024, 1, 11), (new Date(2024, 1, 13))));
        assertTrue(reservation.overlapDate(new Date(2024, 1, 15), (new Date(2024, 1, 16))));
        assertTrue(reservation.overlapDate(new Date(2024, 1, 11), (new Date(2024, 1, 16))));

    }

    @Test
    void outsideDates() throws Exception {
        Reservation reservation = new Reservation();
        Date from = new Date(2024, 1, 13);
        Date to = new Date(2024, 1, 15);
        reservation.countAllFreePositions(from, to);
        assertFalse(reservation.overlapDate(new Date(2024, 1, 12), (new Date(2024, 1, 12))));
        assertFalse(reservation.overlapDate(new Date(2024, 1, 16), (new Date(2024, 1, 17))));
        assertFalse(reservation.overlapDate(new Date(2024, 1, 23), (new Date(2024, 1, 26))));
    }
}
