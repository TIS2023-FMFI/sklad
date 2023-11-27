package Entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "stored_on_position", schema = "public", catalog = "storage")
public class StoredOnPosition {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "pnr")
    private String pnr;
    @Basic
    @Column(name = "id_product")
    private int idProduct;
    @Basic
    @Column(name = "quantity")
    private int quantity;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPnr() {
        return pnr;
    }

    public void setPnr(String pnr) {
        this.pnr = pnr;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StoredOnPosition that = (StoredOnPosition) o;
        return id == that.id && idProduct == that.idProduct && quantity == that.quantity && Objects.equals(pnr, that.pnr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pnr, idProduct, quantity);
    }
}
