package app;

import Entity.*;
import Exceptions.MaterialNotAvailable;
import Exceptions.UserDoesNotExist;
import Exceptions.WrongPassword;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class DatabaseHandler {

    private static SessionFactory sessionFactory;

    public DatabaseHandler() {
        setUpSessionFactory();
    }

    private void setUpSessionFactory() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            sessionFactory.createEntityManager();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
            throw e;
        }
    }

    /***
     * A destructor that closes session factory after exiting application.
     */
    /*
    @Override
    protected void finalize() {
        if (sessionFactory != null) {
            //sessionFactory.close();
        }
    }
     */

    protected boolean savePositionsToDB(List<Position> positions) {
        try (Session session = sessionFactory.openSession()) {
            List<Position> newPositions = new ArrayList<>();
            List<Position> updatePositions = new ArrayList<>();

            for (Position p : positions) {
                if (session.get(Position.class, p.getName()) != null) {
                    updatePositions.add(p);
                } else {
                    newPositions.add(p);
                }
            }

            Transaction transaction = session.beginTransaction();

            for (Position p : positions) {
                session.merge(p);
            }

            transaction.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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
                if (Integer.parseInt(p.getName().substring(3, 4)) % 2 == 0) {
                    row = row + "p";
                } else {
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
     * Method, that loads data to the memory.
     * @return The map of rows and positions.
     */
    public Map<String, Map<Integer, List<Position>>> loadPositionsInRows(){
        try (Session session = sessionFactory.openSession()) {
            Map<String, Map<Integer, List<Position>>> positionsInGroups = new TreeMap<>(new RowNameComparator());
            Query query = session.createQuery("from Position");
            List<Position> positions = query.list();

            for (Position position : positions) {
                String row = getRowName(position.getName());

                if (!positionsInGroups.containsKey(row)){
                    Map<Integer, List<Position>> rowAndPositions = new TreeMap<>(Comparator.reverseOrder());
                    positionsInGroups.put(row, rowAndPositions);
                }

                int shelfNumber = Integer.parseInt(String.valueOf(position.getName().charAt(4)));

                if (!positionsInGroups.get(row).containsKey(shelfNumber)){
                    positionsInGroups.get(row).put(shelfNumber, new ArrayList<>());
                }

                positionsInGroups.get(row).get(shelfNumber).add(position);
            }
            return positionsInGroups;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /***
     * Method, that loads data to the memory.
     * @return The map of rows and positions.
     */
    public Map<String, Map<Integer, Map<Integer, List<Position>>>> getPositionInGroups(){
        try (Session session = sessionFactory.openSession()) {
            Map<String, Map<Integer, Map<Integer, List<Position>>>> positionsInRows = new TreeMap<>(new RowNameComparator());
            Query query = session.createQuery("from Position");
            List<Position> positions = query.list();

            for (Position position : positions) {
                String row = getRowName(position.getName());

                if (!positionsInRows.containsKey(row)){
                    Map<Integer, Map<Integer, List<Position>>> rowAndPositions = new TreeMap<>(Comparator.reverseOrder());
                    positionsInRows.put(row, rowAndPositions);
                }

                int shelfNumber = Integer.parseInt(String.valueOf(position.getName().charAt(4)));

                if (!positionsInRows.get(row).containsKey(shelfNumber)){
                    positionsInRows.get(row).put(shelfNumber, new TreeMap<>(Comparator.reverseOrder()));
                }
                int index = position.getIndex();

                if (!positionsInRows.get(row).get(shelfNumber).containsKey(index)){
                    positionsInRows.get(row).get(shelfNumber).put(index, new ArrayList<>());
                }

                positionsInRows.get(row).get(shelfNumber).get(index).add(position);
            }
            return positionsInRows;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /***
     * Method, that removes a material from database after a successful order.
     * @param material The material to be removed.
     * @param quantity The quantity of the material to be removed.
     * @param position The position from which the material is removed.
     * @param pallet The pnr of the pallet from which the material is removed.
     */
    public void removeItem(Position position, Pallet pallet, Material material, int quantity, boolean removePallet) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query<StoredOnPallet> query = session.createQuery("from StoredOnPallet sop where sop.pnr = :pnr and sop.idProduct = :id");
            query.setParameter("pnr", pallet.getPnr());
            query.setParameter("id", material.getId());
            StoredOnPallet sop = query.getSingleResult();
            if (sop.getQuantity() == quantity){
                session.delete(sop);
                if (removePallet) {
                    Query<PalletOnPosition> popQ = session.createQuery("from PalletOnPosition pop where pop.idPallet = :pnr and pop.idPosition = :id");
                    popQ.setParameter("pnr", pallet.getPnr());
                    popQ.setParameter("id", position.getName());
                    PalletOnPosition pop = popQ.getSingleResult();
                    session.delete(pop);
                    session.delete(pallet);
                }
            }else {
                int quantityOnPallet = getMaterialQuantityOnPallet(pallet, material);
                sop.setQuantity(quantityOnPallet - quantity);
                session.update(sop);
            }
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Customer getCustomerThatReservedPosition(Position position) {
        LocalDate today = LocalDate.now();

        try (Session session = sessionFactory.openSession()) {
            Query<Customer> query = session.createQuery(
                    "SELECT c FROM Customer c " +
                            "JOIN CustomerReservation cr ON c.id = cr.idCustomer " +
                            "WHERE cr.idPosition = :position " +
                            "AND :today BETWEEN cr.reservedFrom AND cr.reservedUntil"
            );

            query.setParameter("position", position.getName());
            query.setParameter("today", today);

            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public void persistMaterialOnPallet(Pallet pallet, Material material, int quantity) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query<StoredOnPallet> query = session.createQuery("from StoredOnPallet sop where sop.pnr = :pnr and sop.idProduct = :id");
            query.setParameter("pnr", pallet.getPnr());
            query.setParameter("id", material.getId());
            if (query.getResultList().size() > 0) {
                StoredOnPallet sop = query.getSingleResult();
                sop.setQuantity(sop.getQuantity() + quantity);
                session.update(sop);
            } else {
                StoredOnPallet sop = new StoredOnPallet();
                sop.setPnr(pallet.getPnr());
                sop.setIdProduct(material.getId());
                sop.setQuantity(quantity);
                session.save(sop);
            }
            transaction.commit();
        } catch (Exception e) {
            setUpSessionFactory();
            e.printStackTrace();
        }
    }

    public void updatePalletPosition(String pallet, String pos) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query<PalletOnPosition> query = session.createQuery("from PalletOnPosition pop " +
                    "where pop.idPallet = :pnr");
            query.setParameter("pnr", pallet);
            PalletOnPosition pops = query.getResultList().get(0);
            System.out.println(pops.getIdPosition() + " " + pos);
            pops.setIdPosition(pos);
            session.update(pops);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addPallet(Pallet pallet, Position finalPosition) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(pallet);
            PalletOnPosition pops = new PalletOnPosition();
            pops.setIdPallet(pallet.getPnr());
            pops.setIdPosition(finalPosition.getName());
            session.save(pops);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Position> getPositionsOfPallet(String palletFrom) {
        try (Session session = sessionFactory.openSession()) {
            Query<Position> query1 = session.createQuery("from Position p join " +
                    "PalletOnPosition pop on pop.idPosition=p.name where pop.idPallet = :name");
            query1.setParameter("name", palletFrom);
            return query1.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Users> getUsers() {
        try (Session session = sessionFactory.openSession()) {
            Query<Users> query = session.createQuery("from Users");
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Users getUser(String selectedUser) {
        try (Session session = sessionFactory.openSession()) {
            Query<Users> query = session.createQuery("from Users where name = :name");
            query.setParameter("name", selectedUser);
            return query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateUser(int id, String newName, String newPassword, boolean newIsAdmin) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query<Users> query = session.createQuery("from Users where id = :id");
            query.setParameter("id", id);
            Users user = query.getSingleResult();
            user.setName(newName);
            user.setPassword(newPassword);
            user.setAdmin(newIsAdmin);
            session.update(user);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addUser(String newName, String newPassword, boolean newIsAdmin) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Users user = new Users();
            user.setName(newName);
            user.setPassword(newPassword);
            user.setAdmin(newIsAdmin);
            session.save(user);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class PositionNumberComparator implements Comparator<Position> {
        @Override
        public int compare(Position position1, Position position2) {
            int number1 = Integer.parseInt(position1.getName().substring(1, 5));
            int number2 = Integer.parseInt(position2.getName().substring(1, 5));

            return Integer.compare(number2, number1);
        }
    }


    /***
     * Comparator for the map positionsInRows. It sorts the map firstly by the alphabetical order and then if two
     * rows have the same name it sorts them by parity, first goes the odd one (n) and then the even (p)
     */
    public class RowNameComparator implements Comparator<String> {
        @Override
        public int compare(String row1, String row2) {
            char firstChar1 = row1.charAt(0);
            char secondChar1 = row1.charAt(1);

            char firstChar2 = row2.charAt(0);
            char secondChar2 = row2.charAt(1);

            int result = Character.compare(firstChar1, firstChar2);

            if (result == 0) {
                result = Character.compare(secondChar1, secondChar2);
            }
            return result;
        }
    }


    /***
     * Method that returns the row name based on its even/odd parity.
     * @param row The name of the row.
     * @return The row name based on its even/odd parity (format: "Ap", "An"...).
     */
    public String getRowName(String row){
        if (Integer.parseInt(row.substring(3, 4)) % 2 == 0) {
            return row.charAt(0) + "p";
        }
        return row.charAt(0) + "n";
    }

    /***
     * Method, that loads data to the memory.
     * @return The map of pallets on positions.
     */
    public Map<Position, Map<Pallet, Map<Material, Integer>>> loadPalletsOnPositions(){
        try (Session session = sessionFactory.openSession()) {
            Map<Position, Map<Pallet, Map<Material, Integer>>> palletsOnPosition = new HashMap<>();
            Query query = session.createQuery("from Position");
            List<Position> positions = query.list();

            for (Position position : positions) {
                List<Pallet> pallets = getPalletesOnPosition(position.getName());
                Map<Pallet, Map<Material, Integer>> materialsOnPallet = new HashMap<>();
                double weight = 0;

                for (Pallet pallet : pallets){
                    Map<Material, Integer> materialAndItsCount = new HashMap<>();
                    List<StoredOnPallet> storedOnPallet = getStoredOnPallet(pallet.getPnr());

                    weight += pallet.getWeight()/pallet.getNumberOfPositions();

                    for (StoredOnPallet product : storedOnPallet){
                        Material material = getMaterial(product.getIdProduct());
                        materialAndItsCount.put(material, product.getQuantity());
                    }
                    materialsOnPallet.put(pallet, materialAndItsCount);
                }
                Warehouse.getInstance().getPositionsWeight().put(position, weight);
                palletsOnPosition.put(position, materialsOnPallet);
            }
            return palletsOnPosition;
        }
        catch (Exception e) {
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
                    session.close();
                    return user;
                }
                else {
                    session.close();
                    throw new WrongPassword(login);
                }
            }
            else {
                session.close();
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
    public Map<Date, Pair<Integer, Integer>> getStatistics(Date dateFrom,
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

            Map<Date, Pair<Integer, Integer>> statistics = new HashMap<>();
            for (History h : result) {
                Date date = h.getDate();
                Pair<Integer, Integer> pair = statistics.get(date);
                if (pair == null) {
                    pair = new Pair<>(0, 0);
                }
                if (h.isTruckIncome()) {
                    pair = new Pair<>(pair.getKey() + h.getNumberOfPallets(), pair.getValue());
                } else {
                    pair = new Pair<>(pair.getKey(), pair.getValue() + h.getNumberOfPallets());
                }
                statistics.put(date, pair);
            }
            session.close();
            return statistics;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ObservableList<String> getCustomersNames() {
        try (Session session = sessionFactory.openSession()) {
            Query<String> query = session.createQuery("SELECT c.name FROM Customer c", String.class);
            return FXCollections.observableArrayList(query.list());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Customer getCustomer(String customerName) {
        try (Session session = sessionFactory.openSession()) {
            Query<Customer> query = session.createQuery("from Customer c where c.name = :name");
            query.setParameter("name", customerName);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /***
     * Method, that returns a material record based on the material name.
     * @param materialName The name of the material.
     * @throws MaterialNotAvailable if the material is not found in the database.
     * @return The material record.
     */
    public Material getMaterial(String materialName) throws MaterialNotAvailable {
        try (Session session = sessionFactory.openSession()) {
            Query<Material> query = session.createQuery("from Material m where m.name = :name");
            query.setParameter("name", materialName);
            if (query.list().size() == 0) {
                throw new MaterialNotAvailable(materialName);
            }
            return query.uniqueResult();
        }
    }

    /***
     * Method that returns a list of records of pallets stored on a given position.
     * @param positionName The position name.
     * @return The list of records of pallets that are stored on the given position.
     */
    public List<Pallet> getPalletesOnPosition(String positionName) {
        try (Session session = sessionFactory.openSession()) {
            Query<PalletOnPosition> query = session.createQuery("from PalletOnPosition pp where pp.idPosition = :name");
            query.setParameter("name", positionName);
            if (query.list().isEmpty()) {
                return new ArrayList<>();
            }
            List<PalletOnPosition> position = query.getResultList();
            List<Pallet> pallets = new ArrayList<>();
            for (PalletOnPosition pp : position) {
                pallets.add(session.get(Pallet.class, pp.getIdPallet()));
            }
            return pallets;
        }
    }

    /***
     * Method, that returns the list of records of materials that are stored on a given pallet.
     * @param pnr The pallet number.
     * @return The list of records of materials that are stored on a given pallet.
     */
    protected List<StoredOnPallet> getStoredOnPallet(String pnr) {
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
    public Position getPosition(String name) {
        try (Session session = sessionFactory.openSession()) {
            Query<Position> query = session.createQuery("from Position p where p.name = :name");
            query.setParameter("name", name);
            return query.uniqueResult();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public Pallet getPallet(String pnr) {
        try (Session session = sessionFactory.openSession()) {
            Query<Pallet> query = session.createQuery("from Pallet p where p.pnr = :pnr");
            query.setParameter("pnr", pnr);
            return query.uniqueResult();
        }
    }

    public int getNumberOfReservations(String customer, Date date) {

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

    /***
     * Method, that returns the list of positions reserved by a given customer.
     * @param customer The name of the customer.
     * @return The list of positions reserved by a given customer.
     */
    public List<Position> getPositionsReservedByCustomer(String customer) {
        try (Session session = sessionFactory.openSession()) {
            int customerId = getCustomer(customer).getId();
            Query<Position> query = session.createQuery("from Position p " +
                    "join CustomerReservation cr on p.name = cr.idPosition where cr.idCustomer = :id");
            query.setParameter("id", customerId);
            return query.getResultList();
        }
    }

    public List<Position> getEmptyPositionsReservedByCustomer(String customer) {
        try (Session session = sessionFactory.openSession()) {
            int customerId = getCustomer(customer).getId();
            Query<Position> query = session.createQuery("from Position p " +
                    "join CustomerReservation cr on p.name = cr.idPosition where cr.idCustomer = :id and" +
                    "(SELECT COUNT(*) FROM PalletOnPosition pop WHERE pop.idPosition = p.name) = 0");
            query.setParameter("id", customerId);
            return query.getResultList();
        }
    }

    private boolean hasMaterial(String customer, String material) {
        try (Session session = sessionFactory.openSession()) {
            List<Position> positions = getPositionsReservedByCustomer(customer);

            for (Position pos : positions) {
                String namePos = pos.getName();
                Query<String> query2 = session.createQuery("select pop.idPallet from PalletOnPosition pop " +
                        "where pop.idPosition = :name");
                query2.setParameter("name", namePos);
                List<String> pnrs = query2.getResultList();
                for (String pnrPal : pnrs) {
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

    public Integer getMaterialQuantityOnPallet(Pallet pallet, Material material) {
        try (Session session = sessionFactory.openSession()) {
            Query<StoredOnPallet> query = session.createQuery("from StoredOnPallet sop where sop.pnr = :pnr and sop.idProduct = :id");
            query.setParameter("pnr", pallet.getPnr());
            query.setParameter("id", material.getId());
            Integer res = 0;
            List<StoredOnPallet> storedOnPallets = query.getResultList();
            for (StoredOnPallet sop : storedOnPallets) {
                res += sop.getQuantity();
            }
            return res;
        }
    }

    /***
     * Method, that returns whether the position is reserved or not .
     * @param positionName The position name.
     * @return True if the PNR is already used, otherwise false.
     */
    public boolean isPositionReservedToday(String positionName) {
        LocalDate today = LocalDate.now();

        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery(
                    "SELECT COUNT(*) FROM CustomerReservation cr " +
                            "WHERE cr.idPosition = :idPosition " +
                            "AND :today BETWEEN cr.reservedFrom AND cr.reservedUntil"
            );

            query.setParameter("idPosition", positionName);
            query.setParameter("today", today);

            return (Long) query.uniqueResult() > 0;
        }
    }

    /***
     * Method, that returns all the positions that are reserved by customer and are/aren´t tall.
     * @param customerId The unique identifier for the customer.
     * @param isTall If the positions should be tall.
     */
    public List<Position> getFreePositions(int customerId, boolean isTall) {
        LocalDate today = LocalDate.now();
        try (Session session = sessionFactory.openSession()) {
            Query<Position> query = session.createQuery("FROM Position p " +
                    "WHERE p.name IN (SELECT cr.idPosition FROM CustomerReservation cr WHERE cr.idCustomer = :customerId " +
                    "AND :today BETWEEN cr.reservedFrom AND cr.reservedUntil) " +
                    "AND p.isTall = :isTall " +
                    "AND p.name NOT IN (SELECT pop.idPosition FROM PalletOnPosition pop)"
            );

            query.setParameter("customerId", customerId);
            query.setParameter("isTall", isTall);
            query.setParameter("today", today);
            return query.list();
        }
    }

    /***
     * Method, that saves pallet information to the database.
     * @param pallet The pallet.
     */
    public void savePalletToDB(Pallet pallet) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(pallet);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * Method, that saves a record of a pallet on a specific position to the database.
     * @param palletId The unique identifier for the pallet (PNR).
     * @param positionId The unique identifier for the position (position name).
     */
    public void savePalletOnPositionToDB(String palletId, String positionId) {
        try (Session session = sessionFactory.openSession()) {
            PalletOnPosition palletOnPosition = new PalletOnPosition();
            palletOnPosition.setIdPallet(palletId);
            palletOnPosition.setIdPosition(positionId);

            session.beginTransaction();
            session.persist(palletOnPosition);
            session.getTransaction().commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * Method, that saves a record of a pallet on a specific position to the database.
     * @param materialName The name of the material.
     */
    public void saveMaterialToDB(String materialName) {
        try (Session session = sessionFactory.openSession()) {
            try {
                Material material = getMaterial(materialName);
            } catch (MaterialNotAvailable e) {
                Material material = new Material();
                material.setName(materialName);

                session.beginTransaction();
                session.persist(material);
                session.getTransaction().commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * Method, that saves a record of a material and its count to the database.
     * @param PRN The unique pallet number.
     * @param materialName The name of the material.
     * @param materialCount The count of the material.
     */
    public void saveMaterialAndCountToDB(String PRN, String materialName, Integer materialCount) {
        try (Session session = sessionFactory.openSession()) {
            StoredOnPallet materialAndCount = new StoredOnPallet();
            materialAndCount.setPnr(PRN);
            materialAndCount.setIdProduct(getMaterial(materialName).getId());
            materialAndCount.setQuantity(materialCount);

            session.beginTransaction();
            session.persist(materialAndCount);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * Method, that checks whether the PNR is already in use.
     * @param PNR The unique pallet number.
     * @return True if the PNR is already used, otherwise false.
     */
    public boolean PNRisUsed(String PNR){
        try (Session session = sessionFactory.openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM Pallet p WHERE p.pnr = :pnr");
            query.setParameter("pnr", PNR);
            return query.uniqueResult() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /***
     * Method, that saves a history record to database.
     * @param customerId The unique identifier for customer.
     * @param numberOfPallets The number of pallets that were stored.
     * @param truckNumber The sequential number representing the arrival order of the truck.
     */
    public void saveHistoryRecord(int customerId, int numberOfPallets, int truckNumber, boolean truckIncome) {
        try (Session session = sessionFactory.openSession()) {
            History history = new History();
            history.setIdCustomer(customerId);
            history.setDate(Date.valueOf(LocalDate.now()));
            history.setTime(Time.valueOf(LocalTime.now()));
            history.setTruckIncome(truckIncome);
            history.setNumberOfPallets(numberOfPallets);
            history.setTruckNumber(truckNumber);

            session.beginTransaction();
            session.persist(history);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * Method, that returns the order of the truck that is currently being loaded.
     * @param id The unique identifier for the customer.
     *           @param now The current date.
     */
    public int getTruckNum(int id, LocalDate now, boolean truckIncome) {
        try (Session session = sessionFactory.openSession()) {
            Query<Integer> query = session.createQuery("SELECT MAX(h.truckNumber) FROM History h WHERE h.idCustomer = :id AND h.date = :date AND h.truckIncome = :truckIncome");
            query.setParameter("id", id);
            query.setParameter("date", now);
            query.setParameter("truckIncome", truckIncome);
            Integer result = query.uniqueResult();
            if (result == null) {
                return 1;
            } else {
                return result + 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    /***
     * Method, that returns a record for a given position name.
     * @param positionName The position name.
     * @return The CustomerReservation record associated with the provided position name,
     */
    public CustomerReservation getReservation(String positionName){
        LocalDate today = LocalDate.now();

        try (Session session = sessionFactory.openSession()) {
            Query<CustomerReservation> query = session.createQuery(
                    "FROM CustomerReservation cr " +
                            "WHERE cr.idPosition = :position " +
                            "AND :today BETWEEN cr.reservedFrom AND cr.reservedUntil"
            );

            query.setParameter("position", positionName);
            query.setParameter("today", today);

            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Method, that returns the username for a given userId.
     * @param userId The unique identifier of the user.
     * @return The username associated with the provided userId.
     */
    public String getUsername(int userId){
        try (Session session = sessionFactory.openSession()) {
            Users user = session.get(Users.class, userId);
            return user.getName();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /***
     * Method, that creates new customer and saves him to the database.
     * @param customer save this customer to database.
     * @return true when new customer was added to database.
     */
    public boolean saveCustomer(Customer customer){
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(customer);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /***
     * Method, that updates customer information and saves him to the database.
     * @param customer which customer should be updated.
     * @return true when data were updated.
     */
    public boolean updateCustomer(Customer customer, int id){
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Customer customerOld = session.get(Customer.class, id);
            customerOld.setName(customer.getName());
            customerOld.setAddress(customer.getAddress());
            customerOld.setCity(customer.getCity());
            customerOld.setPostalCode(customer.getPostalCode());
            customerOld.setIco(customerOld.getIco());
            customerOld.setDic(customer.getDic());
            session.saveOrUpdate(customerOld);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> getPositionsWithPallet(String PNR){
        try (Session session = sessionFactory.openSession()) {
            Query<String> query = session.createQuery("SELECT pop.idPosition FROM PalletOnPosition pop " +
                    "WHERE pop.idPallet = :pnr");

            query.setParameter("pnr", PNR);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean checkCustomerExist(String name){
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("SELECT COUNT(*) FROM Customer c WHERE c.name = :name");
            query.setParameter("name", name);
            return ! ((Long) query.uniqueResult() > 0);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Position> getAllPositions(){
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("FROM Position p");

            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<CustomerReservation> getAllReservaions(){
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("FROM CustomerReservation");

            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<CustomerReservation> getReservationRecords(int customerId){
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("FROM CustomerReservation r WHERE r.idCustomer = :idCustomer");
            query.setParameter("idCustomer", customerId);

            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean reservePositionForCustomer(int customerId, Date reservedFrom, Date reservedUntil, Position position){
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            CustomerReservation customerReservation = new CustomerReservation(customerId, reservedFrom, reservedUntil, position.getName());
            session.persist(customerReservation);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Customer> getCustomerFromReservedPosition(Date reservedFrom, Date reservedUntil, Position position){
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("SELECT idCustomer FROM CustomerReservation r WHERE r.idPosition = :idPostion AND r.reservedFrom = :reservedFrom" +
                    " and r.reservedUntil = :reservedUntil");
            query.setParameter("idPosition", position.getName());
            query.setParameter("reservedFrom", reservedFrom);
            query.setParameter("reservedUntil", reservedUntil);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isPositionReserevedByCustomer(Date reservedFrom, Date reservedUntil, Position position, Customer c){
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("SELECT idCustomer FROM CustomerReservation r WHERE r.idPosition = :idPosition AND r.reservedFrom = :reservedFrom" +
                    " and r.reservedUntil = :reservedUntil and r.idCustomer = :idCustomer");
            query.setParameter("idPosition", position.getName());
            query.setParameter("reservedFrom", reservedFrom);
            query.setParameter("reservedUntil", reservedUntil);
            query.setParameter("idCustomer", c.getId());
            return query.getResultList().size()>0;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean deleteReservationRecord(Customer customer, Date dateFrom, Date dateUntil){
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
//            session.getTransaction();
            Query query = session.createQuery("DELETE FROM CustomerReservation r WHERE r.reservedFrom = :reservedFrom" +
                    " and r.reservedUntil = :reservedUntil and r.idCustomer = :idCustomer");
            query.setParameter("reservedFrom", dateFrom);
            query.setParameter("reservedUntil", dateUntil);
            query.setParameter("idCustomer", customer.getId());
            query.executeUpdate();
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    protected List<String> getAllUsedPositions(){
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query query = session.createQuery("SELECT pop.idPosition FROM PalletOnPosition pop");
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void changeUserActivity(int id, String palletFrom) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query query = session.createQuery("UPDATE Pallet p SET p.idUser = :idUser WHERE p.pnr = :idPallet");
            query.setParameter("idUser", id);
            query.setParameter("idPallet", palletFrom);
            query.executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean deleteCustomer(String customerName){
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query query = session.createQuery("DELETE FROM Customer c WHERE c.name = :name");
            query.setParameter("name", customerName);
            query.executeUpdate();
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
