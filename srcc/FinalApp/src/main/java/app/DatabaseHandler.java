package app;

import Entity.*;
import Exceptions.MaterialNotAvailable;
import Exceptions.UserDoesNotExist;
import Exceptions.WrongPassword;
import jakarta.persistence.criteria.CriteriaBuilder;
import javafx.util.Pair;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.sql.Date;
import java.util.ArrayList;
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
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("from Position");
            List<Position> positions = query.list();

            Map<String, List<Position>> data = new HashMap<>();
            for (Position p : positions) {
                String row = p.getName().substring(0, 1);
                if (Integer.parseInt(p.getName().substring(3,4))%2==0){
                    row = row + "p";
                }else {
                    row = row + "n";
                }
                if (!data.containsKey(row)) {
                    data.put(row, new ArrayList<>());
                }
                data.get(row).add(p);
            }
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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

    public Customer getCustomer(String toString) {
        try (Session session = sessionFactory.openSession()) {
            Query<Customer> query = session.createQuery("from Customer c where c.name = :name");
            query.setParameter("name", toString);
            return query.uniqueResult();
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

    public Material getMaterial(String text) throws MaterialNotAvailable{
        try (Session session = sessionFactory.openSession()) {
            Query<Material> query = session.createQuery("from Material m where m.name = :name");
            query.setParameter("name", text);
            if (query.list().size() == 0) {
                throw new MaterialNotAvailable(text);
            }
            return query.uniqueResult();
        }
    }

    protected Pallet getPallet(String text){
        try (Session session = sessionFactory.openSession()) {
            Query<Pallet> query = session.createQuery("from Pallet p where p.pnr = :name");
            query.setParameter("name", text);
            if (query.list().size() == 0) {
                return null;
            }
            return query.uniqueResult();
        }
    }

    protected List<String> getPalletesOnPosition(String name){
        try (Session session = sessionFactory.openSession()) {
            Query<PalletOnPosition> query = session.createQuery("from PalletOnPosition pp where pp.idPosition = :name");
            query.setParameter("name", name);
            if (query.list().size() == 0) {
                return new ArrayList<>();
            }
            List<PalletOnPosition> position = query.getResultList();
            List<String> pallets = new ArrayList<>();
            for (PalletOnPosition pp : position) {
                pallets.add(pp.getIdPallet());
            }
            return pallets;
        }
    }

    /***
     * Method, that returns the list of records of materials that are stored on a given pallet.
     * @param pnr The pallet number.
     * @return The list of records of materials that are stored on a given pallet.
     */
    protected List<StoredOnPallet> getStoredOnPallet(String pnr){
        try (Session session = sessionFactory.openSession()) {
            Query<StoredOnPallet> query = session.createQuery("from StoredOnPallet sop where sop.pnr = :pnr");
            query.setParameter("pnr", pnr);
            if (query.list().size() == 0) {
                return new ArrayList<>();
            }
            return query.getResultList();
        }
    }

    protected Material getMaterial(int id) {
        try (Session session = sessionFactory.openSession()) {
            Query<Material> query = session.createQuery("from Material m where m.id = :id");
            query.setParameter("id", id);
            return query.uniqueResult();
        }
    }

    public int getNumberOfReservations(String customer, Date date){
        try (Session session = sessionFactory.openSession()) {
            int customerId = getCustomer(customer).getId();
            Query<CustomerReservation> query = session.createQuery("from CustomerReservation r where r.idCustomer = :id");
            query.setParameter("id", customerId);
            List<CustomerReservation> reservations = query.getResultList();
            int numberOfReservations = 0;
            for (CustomerReservation r : reservations) {
                Date from = r.getReservedFrom();
                Date to = r.getReservedUntil();
                if (date.after(from) && date.before(to) || date.equals(from) || date.equals(to)) {
                    numberOfReservations++;
                }
            }
            return numberOfReservations;
        }
    }

    private boolean hasMaterial(String customer, String material) {
        try (Session session = sessionFactory.openSession()) {
            int customerId = getCustomer(customer).getId();
            Query<String> query = session.createQuery("select p.name from Position p " +
                    "join CustomerReservation cr on p.id = cr.idPosition where cr.idCustomer = :id");
            query.setParameter("id", customerId);
            List<String> idsOfPositionsOfCustomer = query.getResultList();

            for (String namePos : idsOfPositionsOfCustomer) {
                Query<String> query2 = session.createQuery("select pop.idPallet from PalletOnPosition pop " +
                        "where pop.idPosition = :name");
                query2.setParameter("name", namePos);
                List<String> pnrs = query2.getResultList();
                for (String pnrPal:pnrs) {
                    Query<String> query3 = session.createQuery("select m.name from Material m " +
                            "join StoredOnPallet sop on m.id = sop.idProduct where sop.pnr = :pnr and m.name = :name");
                    query3.setParameter("name", material);
                    query3.setParameter("pnr", pnrPal);
                    if (query3.getResultList().size() > 0) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
    /***
     * Method, that returns the list of materials that belong to a specific customer.
     * @param customer The customer.
     * @return The list of names of materials.
     */
    public List<String> getMaterials(Customer customer) {
        try (Session session = sessionFactory.openSession()) {
            List<Material> materials = session.createQuery("from Material m").list();
            List<String> names = new ArrayList<>();
            for (Material m : materials) {
                if (hasMaterial(customer.getName(), m.getName())) {
                    names.add(m.getName());
                }
            }
            return names;
        }
    }
}
