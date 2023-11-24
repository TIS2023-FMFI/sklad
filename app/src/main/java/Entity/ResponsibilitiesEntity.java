package Entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "responsibilities", schema = "public", catalog = "testovaciaDatabazaTIS")
public class ResponsibilitiesEntity {
    @Basic
    @Column(name = "iduser")
    private int iduser;
    @Basic
    @Column(name = "idproduct")
    private int idproduct;
    @Id
    @Basic
    @Column(name = "id")
    private int id;

    public int getIduser() {
        return iduser;
    }

    public void setIduser(int iduser) {
        this.iduser = iduser;
    }

    public int getIdproduct() {
        return idproduct;
    }

    public void setIdproduct(int idproduct) {
        this.idproduct = idproduct;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResponsibilitiesEntity that = (ResponsibilitiesEntity) o;
        return iduser == that.iduser && idproduct == that.idproduct && id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(iduser, idproduct, id);
    }

}
