package org.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Responsibilities {
    @Basic
    @Column(name = "idUser")
    private int idUser;
    @Basic
    @Column(name = "idProduct")
    private int idProduct;
    @Basic
    @Column(name = "id")
    private int id;

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
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

        Responsibilities that = (Responsibilities) o;

        if (idUser != that.idUser) return false;
        if (idProduct != that.idProduct) return false;
        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idUser;
        result = 31 * result + idProduct;
        result = 31 * result + id;
        return result;
    }
}
