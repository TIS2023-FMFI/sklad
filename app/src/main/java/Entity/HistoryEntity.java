package Entity;

import jakarta.persistence.*;

import java.sql.Date;
import java.sql.Time;
import java.util.Objects;

@Entity
@Table(name = "history", schema = "public", catalog = "testovaciaDatabazaTIS")
public class HistoryEntity {
    @Basic
    @Column(name = "id")
    private int id;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_customer")
    private int idCustomer;
    @Basic
    @Column(name = "time")
    private Time time;
    @Basic
    @Column(name = "date")
    private Date date;
    @Basic
    @Column(name = "truck_in_out")
    private boolean truckInOut;
    @Basic
    @Column(name = "number_of_pallets")
    private int numberOfPallets;
    @Basic
    @Column(name = "order_number")
    private int orderNumber;

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

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isTruckInOut() {
        return truckInOut;
    }

    public void setTruckInOut(boolean truckInOut) {
        this.truckInOut = truckInOut;
    }

    public int getNumberOfPallets() {
        return numberOfPallets;
    }

    public void setNumberOfPallets(int numberOfPallets) {
        this.numberOfPallets = numberOfPallets;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistoryEntity that = (HistoryEntity) o;
        return id == that.id && idCustomer == that.idCustomer && truckInOut == that.truckInOut && numberOfPallets == that.numberOfPallets && orderNumber == that.orderNumber && Objects.equals(time, that.time) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idCustomer, time, date, truckInOut, numberOfPallets, orderNumber);
    }
}
