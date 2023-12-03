package Entity;

import Exceptions.UserDoesNotExist;
import Exceptions.WrongPassword;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Map;

public class DatabaseHandler {

    private static SessionFactory sessionFactory;
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
    public static Users checkUser(String login, String password) throws UserDoesNotExist, WrongPassword {
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("from Users u where u.name = :login");
            query.setParameter("login", login);
            List<Users> users = query.list();

            if (users.size() == 1) {
                Users user = users.get(0);

                if (user.getPassword().equals(password)) {
                    return user;
                }
                else {
                    throw new WrongPassword(login);
                }
            }
            else {
                throw new UserDoesNotExist(login);
            }
        }
        catch (UserDoesNotExist | WrongPassword e) {
            throw e;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
