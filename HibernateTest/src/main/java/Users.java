import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Date;

@Entity
@Table(name = "users")
public class Users {
    @Id
    long id;

    String meno;

    @Column(name = "datum")
    Date datum_narodenia;

    public Users() {}

    public Users(int id, String meno, Date datum) {
        this.id = id;
        this.meno = meno;
        this.datum_narodenia = datum;
    }
}
