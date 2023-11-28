package Entity;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "customer_reservation", schema = "public", catalog = "storage")
public class CustomerReservation {
    @Id
    @Basic
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "id_customer")
    private int idCustomer;
    @Basic
    @Column(name = "reserved_from")
    private Date reservedFrom;
    @Basic
    @Column(name = "reserved_until")
    private Date reservedUntil;
    @Basic
    @Column(name = "id_position")
    private String idPosition;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(int idCustomer) {
        this.idCustomer = idCustomer;
    }

    public Date getReservedFrom() {
        return reservedFrom;
    }

    public void setReservedFrom(Date reservedFrom) {
        this.reservedFrom = reservedFrom;
    }

    public Date getReservedUntil() {
        return reservedUntil;
    }

    public void setReservedUntil(Date reservedUntil) {
        this.reservedUntil = reservedUntil;
    }

    public String getIdPosition() {
        return idPosition;
    }

    public void setIdPosition(String idPosition) {
        this.idPosition = idPosition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerReservation that = (CustomerReservation) o;
        return id == that.id && idCustomer == that.idCustomer && Objects.equals(reservedFrom, that.reservedFrom) && Objects.equals(reservedUntil, that.reservedUntil) && Objects.equals(idPosition, that.idPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idCustomer, reservedFrom, reservedUntil, idPosition);
    }

}
