package Entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "position", schema = "public", catalog = "testovaciaDatabazaTIS")
public class PositionEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "is_high")
    private boolean isHigh;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHigh() {
        return isHigh;
    }

    public void setHigh(boolean high) {
        isHigh = high;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PositionEntity that = (PositionEntity) o;
        return isHigh == that.isHigh && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, isHigh);
    }
}
