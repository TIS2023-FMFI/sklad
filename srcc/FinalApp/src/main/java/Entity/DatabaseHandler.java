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
        query = "from Users where name = :login and password = :password")

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

    /***
     * Method, that checks if the user exists in the database.
     * @param login The login provided by the user.
     * @param password The password provided by the user.
     * @return The user if exists, null otherwise.
     */
    public Users checkUser(String login, String password) {
        try (Session session = sessionFactory.openSession()) {
            //Query<Users> query = session.createNamedQuery("User_findByNameAndPassword", Users.class); //pokus o prácu s named querry ale neuspešný

            Query query = session.createQuery("from Users u where u.name = :login and u.password = :password");
            query.setParameter("login", login);
            query.setParameter("password", password);
            List<Users> users = query.list();
            if (users.size() == 1) {
                return users.get(0);
            }
        }
        return null;
    }

    /***
     * A destructor that closes session factory after exiting application.
     */
    @Override
    protected void finalize(){
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }
    }
}
