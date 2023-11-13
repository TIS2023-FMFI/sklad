package entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Responsibilities {
    @Basic
    @Column(name = "iduser")
    private Integer idUser;
    @Basic
    @Column(name = "idproduct")
    private Integer idproduct;
    @Id
    @Basic
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdproduct() {
        return idproduct;
    }

    public void setIdproduct(int idProduct) {
        this.idproduct = idProduct;
    }

//    public int getId() {
//        return id;
//    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Responsibilities that = (Responsibilities) o;
        return idUser == that.idUser && idproduct == that.idproduct && id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUser, idproduct, id);
    }



    public Responsibilities() {
    }

    public Responsibilities(Integer idUser, Integer idproduct, Integer id) {
        this.idUser = idUser;
        this.idproduct = idproduct;
        this.id = id;
    }

    public Responsibilities(Integer  idUser, Integer idproduct) {
        this.idUser = idUser;
        this.idproduct = idproduct;
    }
//    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Responsibilities{" +
                "idUser=" + idUser +
                ", idproduct=" + idproduct +
                '}';
    }
}
