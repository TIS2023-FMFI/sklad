package Entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "pallet_on_position", schema = "public", catalog = "storage")
public class PalletOnPosition {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    @Basic
    @Column(name = "id_pallet")
    private String idPallet;

    @Basic
    @Column(name = "id_position")
    private String idPosition;

    public PalletOnPosition() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdPallet() {
        return idPallet;
    }

    public void setIdPallet(String idPallet) {
        this.idPallet = idPallet;
    }

    public String getIdPosition() {
        return idPosition;
    }

    public void setIdPosition(String idPosition) {
        this.idPosition = idPosition;
    }

    @Override
    public String toString() {
        return "PalletOnPosition{" +
                "id=" + id +
                ", idPallet=" + idPallet +
                ", idPosition=" + idPosition +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PalletOnPosition that = (PalletOnPosition) o;
        return id == that.id && idPallet == that.idPallet && idPosition == that.idPosition;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idPallet, idPosition);
    }

    public PalletOnPosition(String idPallet, String idPosition) {
        this.idPallet = idPallet;
        this.idPosition = idPosition;
    }
}
