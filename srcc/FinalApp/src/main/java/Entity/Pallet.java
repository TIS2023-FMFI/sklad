package Entity;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.Objects;

@Entity
public class Pallet {
    @Id
    @Column(name = "pnr")
    private String pnr;
    @Basic
    @Column(name = "weight")
    private int weight;
    @Basic
    @Column(name = "date_income")
    private Date dateIncome;
    @Basic
    @Column(name = "is_damaged")
    private boolean isDamaged;
    @Basic
    @Column(name = "id_user")
    private int idUser;
    @Basic
    @Column(name = "type")
    private String type;
    @Basic
    @Column(name = "note")
    private String note;

    public Pallet() {}

    public String getPnr() {
        return pnr;
    }

    public void setPnr(String pnr) {
        this.pnr = pnr;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Date getDateIncome() {
        return dateIncome;
    }

    public void setDateIncome(Date dateIncome) {
        this.dateIncome = dateIncome;
    }

    public boolean isDamaged() {
        return isDamaged;
    }

    public void setDamaged(boolean damaged) {
        isDamaged = damaged;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pallet pallete = (Pallet) o;
        return weight == pallete.weight && isDamaged == pallete.isDamaged && idUser == pallete.idUser && Objects.equals(pnr, pallete.pnr) && Objects.equals(dateIncome, pallete.dateIncome) && Objects.equals(type, pallete.type) && Objects.equals(note, pallete.note);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pnr, weight, dateIncome, isDamaged, idUser, type, note);
    }
}
