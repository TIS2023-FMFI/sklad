import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Date;

@Entity
public class Projekt {
    @Id
    long id;
    String meno;
    String rola;
    public static void main(String[] args) {
        Date d = new Date(2001, 7, 4);
        System.out.println();
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getId() {
        return id;
    }
    public Projekt() {}

    public Projekt(int id, String meno, String rola) {
        this.id = id;
        this.meno = meno;
        this.rola = rola;
    }

    @Override
    public String toString() {
        return "Projekt{" +
                "id=" + id +
                ", meno='" + meno + '\'' +
                ", rola='" + rola + '\'' +
                '}';
    }
}
