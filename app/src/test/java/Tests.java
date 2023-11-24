import Entity.ProductEntity;
import Entity.ResponsibilitiesEntity;
import Entity.UsersEntity;
import Entity.ProjektEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
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

    }

    @Test
    void removeFromDB(){
        ;
    }

    @Test
    void hql_fetch_data(){
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
//

            session.getTransaction().commit();
        }
    }

    @Test
    public void foreignKeys() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<ResponsibilitiesEntity> responsibilities = session.createQuery("SELECT r.iduser, r.idproduct from ResponsibilitiesEntity r", ResponsibilitiesEntity.class).list();

            for (ResponsibilitiesEntity r : responsibilities) {
                UsersEntity u = session.get(UsersEntity.class, r.getIduser());
                ProductEntity p = session.get(ProductEntity.class, r.getIdproduct());
                System.out.println(r);
                System.out.println("Pouzivatel " + u.getName() + " je zodpovedny za " + p.getName() + " s vahou: " + p.getWeight() + " kg.");

                // Process userId and productId as needed
            }
            session.getTransaction().commit();
        }
    }
}
