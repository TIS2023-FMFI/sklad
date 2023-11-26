package Entity;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "pallete", schema = "public", catalog = "storage")
public class PalleteEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Basic
    @Column(name = "id_position")
    private String idPosition;

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
        PalleteEntity that = (PalleteEntity) o;
        return weight == that.weight && isDamaged == that.isDamaged && idUser == that.idUser && Objects.equals(pnr, that.pnr) && Objects.equals(dateIncome, that.dateIncome) && Objects.equals(type, that.type) && Objects.equals(note, that.note) && Objects.equals(idPosition, that.idPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pnr, weight, dateIncome, isDamaged, idUser, type, note, idPosition);
    }
}
