package Entity;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Map;

@org.hibernate.annotations.NamedQuery(name = "User_findByNameAndPassword",
        query = "from User where name = :login and password = :password")

public class DatabaseHandler {

    private SessionFactory sessionFactory;
    public DatabaseHandler() throws Exception {
        setUpSessionFactory();
    }

    private void setUpSessionFactory() throws Exception {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
            throw e;
        }
    }

    public static void saveToDB() {
    }

    public static void removeFromDB() {
    }

    /***
     * Method, that loads data to the memory.
     * @return The map of rows and positions.
     */
    public Map<String, List<Position>> getWarehouseData() {
        return null;
    }

    public User checkUser(String login, String password) {
        try (Session session = sessionFactory.openSession()) {
            //Query<User> query = session.createNamedQuery("User_findByNameAndPassword", User.class);
            Query query = session.createQuery("select u from User u where u.name = :login and u.password = :password");
            query.setParameter("login", login);
            query.setParameter("password", password);
            List<User> users = query.list();
            if (users.size() == 1) {
                return users.get(0);
            }
        }
        return null;
    }

    @Override
    protected void finalize(){
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }
    }
}
