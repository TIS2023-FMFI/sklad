import entity.Product;
import entity.Responsibilities;
import entity.Users;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

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
        Users addUser = new Users( "Ali", "Nič");
        System.out.println(addUser);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(addUser);
            session.getTransaction().commit();
        }
    }

    @Test
    void removeFromDB(){
        Projekt newProjekt = new Projekt();
        newProjekt.setId(4L);

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<String> allUsers = session.createQuery("select u.name from Users u", String.class).list();
            System.out.println(allUsers.size());
            System.out.println(allUsers);

            Users user = (Users) session.createQuery("select u from Users u where u.name='Ali'", Users.class).uniqueResult();
            session.remove(user);
//            session.delete(newProjekt);

            allUsers = session.createQuery("select u.name from Users u", String.class).list();
            System.out.println(allUsers.size());
            System.out.println(allUsers);
            session.getTransaction().commit();
        }
    }

    @Test
    void hql_fetch_data(){
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
//            List<String> projekt = session.createQuery("select p.meno from Projekt p where rola='Implementacia'", String.class).list();
//            projekt.forEach(System.out::println);
//            List<Projekt> projekt = session.createQuery("select p from Projekt p order by meno", Projekt.class).list();
//            projekt.forEach(System.out::println);
//            System.out.println(session.createQuery("select sum(id) from Projekt p", Projekt.class).list().get(0));
//
//            List<String> projekt = session.createQuery("select p.rola from Projekt p join Users u " +
//                    "on u.meno=p.meno where u.datum_narodenia=", String.class).list();
//            projekt.forEach(System.out::println);

            List<String> allUsers = session.createQuery("select u.name from Users u", String.class).list();
            System.out.println(allUsers);

            List<String> query = session.createQuery("select u.name from Users u join Projekt p on p.id=u.id where p.rola='Komunikacia'", String.class).list();
            query.forEach(System.out::println);

            session.getTransaction().commit();
        }
    }

    @Test
    public void foreignKeys() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Responsibilities> responsibilities = session.createQuery("SELECT r.idUser, r.idproduct from Responsibilities r", Responsibilities.class).list();

            for (Responsibilities r : responsibilities) {
                Users u = session.get(Users.class, r.getIdUser());
                Product p = session.get(Product.class, r.getIdproduct());
                System.out.println(r);
                System.out.println("Pouvatel " + u.getName() + " je zodpovedny za " + p.getName() + " s vahou: " + p.getWeight() + " kg.");

                // Process userId and productId as needed
            }
            session.getTransaction().commit();
        }
    }
}
