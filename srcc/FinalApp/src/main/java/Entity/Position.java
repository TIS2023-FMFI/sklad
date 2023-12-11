package Entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Position {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "is_tall")
    private boolean isTall;

    public Position(String name, boolean isHigh) {
        this.name = name;
        this.isTall = isHigh;
    }

    public Position() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHigh() {
        return isTall;
    }

    public void setHigh(boolean high) {
        isTall = high;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return isTall == position.isTall && Objects.equals(name, position.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, isTall);
    }
}
