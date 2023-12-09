package Entity;

import Exceptions.UserDoesNotExist;
import Exceptions.WrongPassword;
import javafx.util.Pair;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.sql.Date;
import java.util.HashMap;
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

    public static void savePositionsToDB(List<Position> positions) {
        try (Session session = sessionFactory.openSession()) {
            for (Position p : positions) {
                session.persist(p);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
//            return null;
        }
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
     * Method, that returns the statistics for the given period of time for a specific customer.
     * @param dateFrom The date from which the statistics should be calculated.
     * @param dateTo The date to which the statistics should be calculated.
     * @param customerName The name of the customer for which the statistics should be calculated.
     * @return The map of dates and pairs of numbers (number of palets going in and number of
     *         palets going out for that date).
     */
    public Map<Date, Pair<Integer,Integer>> getStatistics(Date dateFrom,
                                                          Date dateTo,
                                                          String customerName) {
        try (Session session = sessionFactory.openSession()) {
             Query q = session.createQuery("select c.id from Customer c where name=:customerName");
            int customerId = (int) q.setParameter("customerName", customerName).uniqueResult();

            Query query = session.createQuery("select h from History h " +
                    "where h.date >= :dateFrom and h.date <= :dateTo and h.idCustomer = :customerId");
            query.setParameter("dateFrom", dateFrom);
            query.setParameter("dateTo", dateTo);
            query.setParameter("customerId", customerId);

            List<History> result = query.list();
            System.out.println(result.size());

            Map<Date, Pair<Integer,Integer>> statistics = new HashMap<>();
            for (History h : result) {
                Date date = h.getDate();
                Pair<Integer,Integer> pair = statistics.get(date);
                if (pair == null) {
                    pair = new Pair<>(0,0);
                }
                if (h.isTruckIncome()) {
                    pair = new Pair<>(pair.getKey() + h.getNumberOfPallets(), pair.getValue());
                }
                else {
                    pair = new Pair<>(pair.getKey(), pair.getValue() + h.getNumberOfPallets());
                }
                statistics.put(date, pair);
            }
            return statistics;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Customer> getCustomers() {
        try (Session session = sessionFactory.openSession()) {
            Query<Customer> query = session.createQuery("from Customer");
            return query.list();
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
