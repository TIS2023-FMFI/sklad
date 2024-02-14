package app;

import Entity.*;
import Exceptions.MaterialNotAvailable;
import Exceptions.UserDoesNotExist;
import Exceptions.WrongPassword;
import jakarta.persistence.NoResultException;
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

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class DatabaseHandler {
    private final double WEIGHT_OF_ONE_POSITION = 500;

    private static SessionFactory sessionFactory;

    /***
     * Constructor that sets up session factory.
     */
    public DatabaseHandler() {
        setUpSessionFactory();
    }
    private Properties loadProperties() {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("C:\\Program Files\\Java\\database.cfg.txt")) {
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    private void setUpSessionFactory() {
        Properties properties = loadProperties();

        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .applySettings(properties)
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
            sessionFactory.close();
        }
    }*/

    /**
     * Saves positions to the database.
     * Positions are either inserted as new records or updated if they already exist.
     * @param positions The list of positions to save.
     * @return true if the save operation is successful, false otherwise.
     */
    public boolean savePositionsToDB(List<Position> positions) {
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
     * Method, that returns a list of positions that satisfy the given parameters of a pallet.
     * @param customer The customer.
     * @param weight The weight of the pallet.
     * @param isTall The height of the pallet.
     * @param numberOfPositions The number of positions.
     * @return The list of positions.
     */
    public List<List<Position>> getFreePositions(Customer customer, double weight, boolean isTall, int numberOfPositions){
        List<List<Position>> result = new ArrayList<>();

        List<List<Position>> reservedPositions = getReservedPositions(customer, weight, isTall, numberOfPositions);
        List<List<Position>> reservedPositionsWithPallets = getReservedPositionsWithPallets(customer, weight, isTall, numberOfPositions);

        if (reservedPositions.isEmpty() && reservedPositionsWithPallets.isEmpty()){
            return result;
        }

        if (!reservedPositions.isEmpty()){
            result.addAll(reservedPositions);
        }
        if (!reservedPositionsWithPallets.isEmpty()){
            result.addAll(reservedPositionsWithPallets);
        }

        // sorting and finding the best position
        List<List<Position>> floorOrTallPositions = new ArrayList<>();

        for (List<Position> positions : result){
            if (positions.get(0).isTall() || (positions.get(0).getName().charAt(4) == '0')){
                floorOrTallPositions.add(positions);
            }
        }
        result.removeAll(floorOrTallPositions);

        Comparator<List<Position>> positionComparator = Comparator
                .comparing((List<Position> list) -> reservedPositionsWithPallets.contains(list))
                .thenComparing(list -> reservedPositions.contains(list))
                .thenComparing(list -> Character.getNumericValue(list.get(0).getName().charAt(4)));


        Collections.sort(result, positionComparator);

        List<List<Position>> reservedFloorOrTallPositions = new ArrayList<>();
        List<List<Position>> reservedWithPalletFloorOrTallPosition = new ArrayList<>();

        for (List<Position> positions : floorOrTallPositions){
            if (reservedPositions.contains(positions)){
                reservedFloorOrTallPositions.add(positions);
            }
            else if (reservedPositionsWithPallets.contains(positions)){
                reservedWithPalletFloorOrTallPosition.add(positions);
            }
        }

        Comparator<List<Position>> floorOrTallComparator = (list1, list2) -> {
            boolean tallList1 = list1.stream().anyMatch(Position::isTall);
            boolean tallList2 = list2.stream().anyMatch(Position::isTall);

            // Sort tall lists last
            if (tallList1 && !tallList2) {
                return 1;
            } else if (!tallList1 && tallList2) {
                return -1;
            } else {
                int shelfComparison = Integer.compare(
                        Character.getNumericValue(list2.get(0).getName().charAt(4)),
                        Character.getNumericValue(list1.get(0).getName().charAt(4))
                );

                // If the lists are not tall, sort by shelf in ascending order
                return (tallList1 || tallList2) ? shelfComparison : -shelfComparison;
            }
        };

        Collections.sort(reservedFloorOrTallPositions, floorOrTallComparator);
        result.addAll(reservedFloorOrTallPositions);

        Collections.sort(reservedWithPalletFloorOrTallPosition, floorOrTallComparator);
        result.addAll(reservedWithPalletFloorOrTallPosition);

        return result;
    }

    /***
     * Method, that returns all the reserved positions that satisfy the given conditions.
     * @param customer The customer for whom positions are reserved.
     * @param weight The weight of the pallet.
     * @param isTall Whether the position is tall.
     * @param numberOfPositions The number of consecutive positions to be selected.
     * @return A list of position sets, each representing a valid selection of reserved positions.
     */
    public List<List<Position>> getReservedPositions(Customer customer, double weight, boolean isTall, int numberOfPositions){
        // all free reserved positions for the customer
        List<Position> allFreeReservedPositions = getAllFreeReservedPositions(customer.getId());
        allFreeReservedPositions = filterPositionsBasedOnWeightHeight(allFreeReservedPositions, isTall, weight);

        List<List<Position>> result = new ArrayList<>();

        for (Position position : allFreeReservedPositions){
            List<Position> possibleNPositions = new ArrayList<>();
            possibleNPositions.add(position);
            int positionIndex = position.getIndex();

            char row = position.getName().charAt(0);
            int positionNumber = Integer.parseInt(position.getName().substring(1, 4));
            char shelf = position.getName().charAt(4);

            boolean areAllSuitable = true;
            for (int i = 1; i < numberOfPositions; i++){
                Position neighbouringPosition = getPosition(String.format("%s%03d%s", row, positionNumber + i*2, shelf));

                if (allFreeReservedPositions.contains(neighbouringPosition) && neighbouringPosition.getIndex() == positionIndex){
                    possibleNPositions.add(neighbouringPosition);
                }
                else {
                    areAllSuitable = false;
                    break;
                }
            }
            if (areAllSuitable){
                if (possibleNPositions.get(0).getName().charAt(4) == '0'){
                    result.add(possibleNPositions);
                }
                else if (weight/numberOfPositions <= WEIGHT_OF_ONE_POSITION){
                    result.add(possibleNPositions);
                }
            }
        }
        return result;
    }

    /***
     * Method, that filters positions based on their height and weight, returning a list of positions that meet specified conditions.
     * @param positions The positions reserved by a customer.
     * @param weight The weight of the pallet.
     * @param isTall Whether the position is tall.
     * @return A filtered list of positions based on the given conditions.
     */
    public List<Position> filterPositionsBasedOnWeightHeight(List<Position> positions, boolean isTall, double weight){
        List<Position> result = positions;
        // filtered positions based on whether tall positions are required
        if (isTall) {
            result = result.stream().filter(Position::isTall).collect(Collectors.toList());
        }
        // filtered positions based on weight (if they weight over 1200 they can only be placed on the ground)
        if (weight > 1200){
            result = getAllFreeReservedFloorPositions(result);
        }
        return result;
    }

    /***
     * Method, that filters out positions that are only on the ground.
     * @param positions The positions reserved by a customer.
     * @return list of positions that are on ground.
     */
    public List<Position> getAllFreeReservedFloorPositions(List<Position> positions) {
        List<Position> result = new ArrayList<>();
        for (Position position : positions) {
            int shelf = Character.getNumericValue(position.getName().charAt(4));
            if (shelf == 0) {
                result.add(position);
            }
        }
        return result;
    }

    /***
     * Method, that returns all the positions that are reserved by customer.
     * @param customerId The unique identifier for the customer.
     */
    public List<Position> getAllFreeReservedPositions(int customerId) {
        LocalDate today = LocalDate.now();
        try (Session session = sessionFactory.openSession()) {
            Query<Position> query = session.createQuery("FROM Position p " +
                    "WHERE p.name IN (SELECT cr.idPosition FROM CustomerReservation cr WHERE cr.idCustomer = :customerId " +
                    "AND :today BETWEEN cr.reservedFrom AND cr.reservedUntil) " +
                    "AND p.name NOT IN (SELECT pop.idPosition FROM PalletOnPosition pop)"
            );

            query.setParameter("customerId", customerId);
            query.setParameter("today", today);
            return query.list();
        }
    }


    /***
     * Method, that returns all the positions that are reserved by customer and have pallets on them.
     * @param customer The unique identifier for the customer.
     * @param weight The weight of the pallet.
     * @param isTall Whether the position is tall.
     * @param numberOfPositions The number of consecutive positions to be selected.
     * @return A list of positions that are reserved by customer and have pallets on them.
     */
    public List<List<Position>> getReservedPositionsWithPallets(Customer customer, double weight, boolean isTall, int numberOfPositions){
        List<Position> allReservedPositionsWithPallet = getAllFreeReservedPositionsWithPallet(customer.getId());
        allReservedPositionsWithPallet = filterPositionsBasedOnWeightHeight(allReservedPositionsWithPallet, isTall, weight);

        List<List<Position>> result = new ArrayList<>();

        for (Position position : allReservedPositionsWithPallet){
            List<Position> possibleNPositions = new ArrayList<>();
            possibleNPositions.add(position);
            int positionIndex = position.getIndex();

            List<Pallet> palletsOnPosition = getPalletesOnPosition(position.getName());
            int numberOfPalletsOnPosition = palletsOnPosition.size();
            double weightOfPalletsOnPosition = getWeightOfPalletsOnPosition(palletsOnPosition);

            char row = position.getName().charAt(0);
            int positionNumber = Integer.parseInt(position.getName().substring(1, 4));
            char shelf = position.getName().charAt(4);

            boolean areAllSuitable = true;
            if (position.getName().charAt(4) != '0' && (weightOfPalletsOnPosition + weight/numberOfPositions) > WEIGHT_OF_ONE_POSITION){
                areAllSuitable = false;
            }
            for (int i = 1; i < numberOfPositions; i++){
                Position neighbouringPosition = getPosition(String.format("%s%03d%s", row, positionNumber + i*2, shelf));

                if (allReservedPositionsWithPallet.contains(neighbouringPosition) && neighbouringPosition.getIndex() == positionIndex){
                    List<Pallet> palletsOnNeighbouringPosition = getPalletesOnPosition(neighbouringPosition.getName());
                    int numberOfPalletsOnNeighbouringPosition = palletsOnNeighbouringPosition.size();
                    double weightOfPalletsOnNeighbouringPosition = getWeightOfPalletsOnPosition(palletsOnNeighbouringPosition);

                    if (numberOfPalletsOnPosition == numberOfPalletsOnNeighbouringPosition) {
                        if (neighbouringPosition.getName().charAt(4) == '0') {
                            possibleNPositions.add(neighbouringPosition);
                        } else if ((weightOfPalletsOnNeighbouringPosition + weight / numberOfPositions) <= WEIGHT_OF_ONE_POSITION) {
                            possibleNPositions.add(neighbouringPosition);
                        }
                    }
                    else {
                        areAllSuitable = false;
                        break;
                    }
                }
                else {
                    areAllSuitable = false;
                    break;
                }
            }
            if (areAllSuitable){
                result.add(possibleNPositions);
            }
        }
        return result;
    }

    /***
     * Method, that calculates the weight of all the pallets on a given position.
     * @param pallets The pallets on the position.
     * @return The weight of all the pallets on a given position.
     */
    public double getWeightOfPalletsOnPosition(List<Pallet> pallets){
        double result = 0;
        for (Pallet pallet : pallets){
            result += pallet.getWeight()/pallet.getNumberOfPositions();
        }
        return result;
    }

    /***
     * Method, that returns all the positions that are reserved by customer that have pallets stored on them.
     * @param customerId The unique identifier for the customer.
     */
    public List<Position> getAllFreeReservedPositionsWithPallet(int customerId) {
        LocalDate today = LocalDate.now();
        try (Session session = sessionFactory.openSession()) {
            Query<Position> query = session.createQuery("FROM Position p " +
                    "WHERE p.name IN (SELECT cr.idPosition FROM CustomerReservation cr WHERE cr.idCustomer = :customerId " +
                    "AND :today BETWEEN cr.reservedFrom AND cr.reservedUntil) " +
                    "AND p.name IN (SELECT pop.idPosition FROM PalletOnPosition pop)"
            );
            query.setParameter("customerId", customerId);
            query.setParameter("today", today);
            return query.list();
        }
    }

    /***
     * Method, that removes a material from database after a successful order.
     * @param material The material to be removed.
     * @param quantity The quantity of the material to be removed.
     * @param positions The positions from which the pallet is removed if it is empty.
     * @param pallet The pnr of the pallet from which the material is removed.
     */
    public void removeItem(List<Position> positions, Pallet pallet, Material material, int quantity, boolean removePallet) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query<StoredOnPallet> query = session.createQuery("from StoredOnPallet sop where sop.pnr = :pnr and sop.idProduct = :id");
            query.setParameter("pnr", pallet.getPnr());
            query.setParameter("id", material.getId());
            StoredOnPallet sop = query.getSingleResult();
            if (sop.getQuantity() == quantity){
                session.delete(sop);
                if (removePallet) {
                    for (Position position : positions) {
                        Query<PalletOnPosition> popQ = session.createQuery("from PalletOnPosition pop where pop.idPallet = :pnr and pop.idPosition = :id");
                        popQ.setParameter("pnr", pallet.getPnr());
                        popQ.setParameter("id", position.getName());
                        PalletOnPosition pop = popQ.getSingleResult();
                        session.delete(pop);
                    }
                    session.delete(pallet);
                }
                transaction.commit();
                checkIfMaterialIsStored(material);
            }else {
                int quantityOnPallet = getMaterialQuantityOnPallet(pallet, material);
                sop.setQuantity(quantityOnPallet - quantity);
                session.update(sop);
                transaction.commit();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkIfMaterialIsStored(Material material) {
        try (Session session = sessionFactory.openSession()) {
            Query<Long> query = session.createQuery("select count(*) from StoredOnPallet sop where sop.idProduct = :id");
            query.setParameter("id", material.getId());
            Long sop = query.getSingleResult();
            if (sop == 0){
                Transaction transaction = session.beginTransaction();
                String hql = "delete from Material where id= :id";
                Query queryDel = session.createQuery(hql);
                queryDel.setParameter("id", material.getId());
                queryDel.executeUpdate();
                transaction.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * Method, that returns customer that has the given position reserved at the moment.
     * @param position The position to be checked.
     * @return The customer that reserved the position.
     */
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

    /**
     * Method that saves a material on a pallet in the database.
     * If the material is already stored on the pallet, its quantity is updated otherwise, a new entry is created.
     * @param pallet The pallet where the material is stored.
     * @param material The material to be persisted.
     * @param quantity The quantity of the material to be persisted.
     */
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

    /**
     * Method that updates the position of a pallet in the database.
     * @param pallet The identifier of the pallet.
     * @param pos The new position to be assigned to the pallet.
     */
    public void updatePalletPosition(String pallet, String pos) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query<PalletOnPosition> query = session.createQuery("from PalletOnPosition pop " +
                    "where pop.idPallet = :pnr");
            query.setParameter("pnr", pallet);
            PalletOnPosition pops = query.getResultList().get(0);
            pops.setIdPosition(pos);
            session.update(pops);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method that saves a pallet to the database and assigns it to a final position.
     * @param pallet The pallet to be added.
     * @param finalPosition The final position to which the pallet is assigned.
     */
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

    /**
     * Method that returns the positions associated with a pallet from the database.
     * @param palletFrom The identifier of the pallet.
     * @return A list of positions associated with the specified pallet.
     */
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

    /**
     * Method that returns a list of all users from the database.
     * @return A list containing all users stored in the database.
     */
    public List<Users> getUsers() {
        try (Session session = sessionFactory.openSession()) {
            Query<Users> query = session.createQuery("from Users");
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Method that returns user from the database based on the specified username.
     * @param selectedUser The username of the user to retrieve.
     * @return The user object corresponding to the specified username, or null if not found.
     */
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

    /**
     * Method that updates the information of a user in the database.
     * @param id The unique identifier of the user.
     * @param newName The new name to be assigned to the user.
     * @param newPassword The new password to be assigned to the user.
     * @param newIsAdmin The new admin status to be assigned to the user.
     */
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

    /**
     * Method that adds a new user to the database.
     * @param newName The name of the new user.
     * @param newPassword The password of the new user.
     * @param newIsAdmin The admin status of the new user.
     */
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

    /**
     * Method that retrieves the root customer from the database.
     * @return The root customer, or null if not found.
     */
    public Customer getRootCustomer() {
        try (Session session = sessionFactory.openSession()){
            Query<Customer> query = session.createQuery("from Customer where isRoot = true");
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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

                for (Pallet pallet : pallets){
                    Map<Material, Integer> materialAndItsCount = new HashMap<>();
                    List<StoredOnPallet> storedOnPallet = getStoredOnPallet(pallet.getPnr());

                    for (StoredOnPallet product : storedOnPallet){
                        Material material = getMaterial(product.getIdProduct());
                        materialAndItsCount.put(material, product.getQuantity());
                    }
                    materialsOnPallet.put(pallet, materialAndItsCount);
                }
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

    /**
     * Method that retrieves the names of all customers from the database.
     * @return An observable list containing the names of all customers, or null if an error occurs.
     */
    public ObservableList<String> getCustomersNames() {
        try (Session session = sessionFactory.openSession()) {
            Query<String> query = session.createQuery("SELECT c.name FROM Customer c", String.class);
            return FXCollections.observableArrayList(query.list());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Method that retrieves a customer by name from the database.
     * @param customerName The name of the customer to retrieve.
     * @return The customer with the specified name, or null if not found or an error occurs.
     */
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
     * Method that returns a material record based on the material name.
     * @param materialName The name of the material.
     * @throws MaterialNotAvailable if the material is not found in the database.
     * @return The material record.
     */
    public Material getMaterial(String materialName) throws MaterialNotAvailable{
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

    /**
     * Method that returns a position by name from the database.
     * @param name The name of the position to retrieve.
     * @return The position with the specified name, or null if not found or an error occurs.
     */
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

    /**
     * Method that returns a pallet by its unique identifier (PNR) from the database.
     * @param pnr The unique identifier of the pallet to retrieve.
     * @return The pallet with the specified unique identifier, or null if not found or an error occurs.
     */
    public Pallet getPallet(String pnr) {
        try (Session session = sessionFactory.openSession()) {
            Query<Pallet> query = session.createQuery("from Pallet p where p.pnr = :pnr");
            query.setParameter("pnr", pnr);
            return query.uniqueResult();
        }
    }

    /**
     * Method that returns the number of reservations for a customer on a specific date from the database.
     * @param customer The name of the customer.
     * @param date The date for which reservations are counted.
     * @return The number of reservations for the specified customer on the specified date.
     */
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

    /**
     * Method that returns the quantity of a material stored on a specific pallet.
     * @param pallet The pallet on which the material is stored.
     * @param material The material.
     * @return The quantity of the material stored on the pallet.
     */
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
     * Method, that returns whether the position is reserved or not.
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
            session.save(customer);
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
            customerOld.setIco(customer.getIco());
            customerOld.setDic(customer.getDic());
            session.saveOrUpdate(customerOld);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Method that returns the positions associated with a pallet.
     * @param PNR The unique identifier of the pallet.
     * @return A list of positions associated with the specified pallet.
     */
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

    /**
     * Method that checks if a customer exists in the database.
     * @param name The name of the customer to check.
     * @return true if the customer exists, false otherwise.
     */
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

    /**
     * Method that returns all positions from the database.
     * @return A list containing all positions stored in the database, or null if an error occurs.
     */
    public List<Position> getAllPositions(){
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("FROM Position p");

            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Method that returns all reservations from the database.
     * @return A list containing all reservations stored in the database, or null if an error occurs.
     */
    public List<CustomerReservation> getAllReservaions(){
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("FROM CustomerReservation");

            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Method that returns all customers from the database.
     * @return A list containing all customers stored in the database, or null if an error occurs.
     */
    public List<Customer> getCustomers(){
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("FROM Customer ");
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Method that returns reservation records for a given customer ID.
     * @param customerId The ID of the customer for which reservation records are retrieved.
     * @return A list containing reservation records for the specified customer, or an empty list if no records are found or an error occurs.
     */
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

    /**
     * Method that reserves a position for a customer.
     * @param customerId The ID of the customer reserving the position.
     * @param reservedFrom The start date of the reservation.
     * @param reservedUntil The end date of the reservation.
     * @param position The position being reserved.
     * @return true if the reservation is successful, false otherwise.
     */
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

    /**
     * Method that checks if a position is reserved by a customer within a specified time frame.
     * @param reservedFrom The start date of the reservation.
     * @param reservedUntil The end date of the reservation.
     * @param position The position to check for reservation.
     * @param c The customer reserving the position.
     * @return true if the position is reserved by the customer within the specified time frame, false otherwise.
     */
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

    /**
     * Method that deletes a reservation record for a customer within a specified time frame.
     * @param customer The customer whose reservation record is being deleted.
     * @param dateFrom The start date of the reservation.
     * @param dateUntil The end date of the reservation.
     * @return true if the deletion is successful, false otherwise.
     */
    public boolean deleteReservationRecord(Customer customer, Date dateFrom, Date dateUntil){
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
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

    /**
     * Method that returns all positions currently in use from the database.
     * @return A list containing the IDs of all positions currently occupied by pallets, or null if an error occurs.
     */
    public List<String> getAllUsedPositions(){
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query query = session.createQuery("SELECT pop.idPosition FROM PalletOnPosition pop");
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Method that updates the activity of a user for a specified pallet.
     * @param id The ID of the user whose activity is being updated.
     * @param palletFrom The PNR of the pallet for which the activity is being updated.
     */
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

    /**
     * Method that deletes a customer from the database.
     * @param customerName The name of the customer to be deleted.
     * @return true if the deletion is successful, false otherwise.
     */
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

    /**
     * Method that deletes a user from the database.
     * @param id The ID of the user to be deleted.
     */
    public void deleteUser(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query query = session.createQuery("DELETE FROM Users u WHERE u.id = :id");
            query.setParameter("id", id);
            query.executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method that deletes pallets from the database.
     * @param pnrs A set of PNRs corresponding to the pallets to be deleted.
     */
    public void deletePallets(Set<String> pnrs){
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            for(var pnr : pnrs){
                Query query = session.createQuery("DELETE FROM Pallet p WHERE p.pnr = :pnr");
                query.setParameter("pnr", pnr);
                query.executeUpdate();
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method that deletes all positions from the database.
     */
    public void deletePositions(){
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query query = session.createQuery("DELETE FROM Position");
            query.executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method that retrieves a list of pallets reserved by a customer with a specific material.
     * @param id The ID of the customer reserving the pallets.
     * @param materialName The name of the material to check for.
     * @return A list of pallets reserved by the customer with the specified material, or null if an error occurs.
     */
    public List<Pallet> getPalletesReservedByCustomerWithMaterial(int id, String materialName) {
        try (Session session = sessionFactory.openSession()) {
            Query<Pallet> query = session.createQuery
                    ("SELECT p FROM Pallet p " +
                            "JOIN PalletOnPosition pop ON p.pnr=pop.idPallet " +
                            "JOIN Position pos ON pop.idPosition=pos.name " +
                            "JOIN CustomerReservation cr ON pos.name=cr.idPosition " +
                            "JOIN StoredOnPallet sop ON p.pnr=sop.pnr " +
                            "WHERE sop.idProduct=:idMat AND cr.idCustomer=:idCus");

            query.setParameter("idCus", id);
            query.setParameter("idMat", getMaterial(materialName).getId());
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
