package Strecno;

import java.util.Date;

public class Reservation {
    private Position position;
    private Customer customer;
    private Date reservationStartDate;
    private Date reservationDateEnd;

    public Reservation(Position position, Customer customer, Date reservationStartDate, Date reservationDateEnd) {
        this.position = position;
        this.customer = customer;
        this.reservationStartDate = reservationStartDate;
        this.reservationDateEnd = reservationDateEnd;
    }

    public Position getPosition() {
        return position;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Date getReservationStartDate() {
        return reservationStartDate;
    }

    public Date getReservationDateEnd() {
        return reservationDateEnd;
    }
}
