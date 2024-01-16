import Entity.Material;
import Entity.Position;
import Entity.Users;
import app.DatabaseHandler;
import app.RelocateProduct;
import app.Warehouse;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class Tests {
    private SessionFactory sessionFactory;

    @BeforeEach
    protected void setUp() throws Exception {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        System.out.println("registry created");
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            System.out.println("Session factory created");
        }
        catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
            System.out.println("Session factory creation failed with exception: " + e);
            throw e;
        }
    }

    @AfterEach
    protected void tearDown() throws Exception {
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }
    }

    @Test
    void testCorrectUserCheck() throws Exception {
        DatabaseHandler db = new DatabaseHandler();
        Users u = db.checkUser("admin", "admin");
        Assertions.assertEquals(Users.class, u.getClass());
    }

    @Test
    void saveToDB(){
        Material newMaterial = new Material();
        newMaterial.setName("Test materialu");
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(newMaterial);
            session.getTransaction().commit();
        }
    }

    @Test
    void removeFromDB(){
        ;
    }

    @Test
    void hql_fetch_data(){
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<String> query = session.createQuery("select u.name from Users u where password='admin'", String.class).list();
            query.forEach(System.out::println);

            //Query query = session.createQuery("select u.name from Customer u");
            //System.out.println("query created");
            //query.getResultList().forEach(System.out::println);

            session.getTransaction().commit();
        }
    }

    @Test
    public void foreignKeys() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.getTransaction().commit();
        }
    }

    @Test
    public void relocation() {

    }
}
