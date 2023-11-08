import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Projekt {
    @Id
    long id;
    String meno;
    String rola;
    public static void main(String[] args) {}

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
}
