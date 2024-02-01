package Entity;

import app.Warehouse;
import jakarta.persistence.*;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Entity
public class Position {
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

    public boolean isTall() {
        return isTall;
    }

    public int getIndex(){
        String n = name.substring(1, 4);

        int index = Integer.parseInt(n);
        if(index % 2 == 0){
            index--;
        }
        index /= 2;
        index /= 4;
        return index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return isTall == position.isTall && Objects.equals(name, position.name);
    }

    @Override
    public String toString() {
        return "Position{" +
                "name='" + name + '\'' +
                ", isTall=" + isTall +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, isTall);
    }
}
