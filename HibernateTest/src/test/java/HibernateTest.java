import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class HibernateTest {
    private SessionFactory sessionFactory;

    @BeforeEach
    protected void setUp() throws Exception {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure() // configures settings from hibernate.cfg.xml
            .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    @AfterEach
    protected void tearDown() throws Exception {
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }
    }

    @Test
    void saveToDB(){
        Projekt newProjekt = new Projekt(4, "Ali", "Nič");
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(newProjekt);
            session.getTransaction().commit();
        }
    }

    @Test
    void removeFromDB(){
        Projekt newProjekt = new Projekt();
        newProjekt.setId(4L);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(newProjekt);
            session.getTransaction().commit();
        }
    }

    @Test
    @Disabled
    public void howDoesHibernateWork() {

    }
}
