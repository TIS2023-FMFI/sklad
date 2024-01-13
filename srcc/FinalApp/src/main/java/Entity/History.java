package Entity;

import jakarta.persistence.*;

import java.sql.Date;
import java.sql.Time;
import java.util.Objects;

@Entity
@Table(name = "history", schema = "public", catalog = "storage")
public class History {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "id_customer")
    private int idCustomer;
    @Basic
    @Column(name = "time")
    private Time time;
    @Basic
    @Column(name = "date")
    private Date date;
    @Basic
    @Column(name = "truck_income")
    private boolean truckIncome;
    @Basic
    @Column(name = "number_of_pallets")
    private int numberOfPallets;
    @Basic
    @Column(name = "truck_number")
    private int truckNumber;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdCustomer(int idCustomer) {
        this.idCustomer = idCustomer;
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

    public boolean isTruckIncome() {
        return truckIncome;
    }

    public void setTruckIncome(boolean truckInOut) {
        this.truckIncome = truckInOut;
    }

    public int getNumberOfPallets() {
        return numberOfPallets;
    }

    public void setNumberOfPallets(int numberOfPallets) {
        this.numberOfPallets = numberOfPallets;
    }

    public void setTruckNumber(int orderNumber) {
        this.truckNumber = orderNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        History history = (History) o;
        return id == history.id && idCustomer == history.idCustomer && truckIncome == history.truckIncome && numberOfPallets == history.numberOfPallets && truckNumber == history.truckNumber && Objects.equals(time, history.time) && Objects.equals(date, history.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idCustomer, time, date, truckIncome, numberOfPallets, truckNumber);
    }
}
