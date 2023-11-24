package Entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "projekt", schema = "public", catalog = "testovaciaDatabazaTIS")
public class ProjektEntity {
    @Basic
    @Column(name = "meno")
    private String meno;
    @Id
    @Basic
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "rola")
    private String rola;

    public String getMeno() {
        return meno;
    }

    public void setMeno(String meno) {
        this.meno = meno;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRola() {
        return rola;
    }

    public void setRola(String rola) {
        this.rola = rola;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjektEntity that = (ProjektEntity) o;
        return Objects.equals(meno, that.meno) && Objects.equals(id, that.id) && Objects.equals(rola, that.rola);
    }

    @Override
    public int hashCode() {
        return Objects.hash(meno, id, rola);
    }
}
