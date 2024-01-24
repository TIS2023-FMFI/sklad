package app;

import Entity.*;
import javafx.util.Pair;

import java.util.*;

public class Reservation {
    Date dateFrom;
    Date dateTo;
    Set<Position> aviablePositions = new HashSet<>();
    DatabaseHandler databaseHandler;

    protected boolean overlapDate(Date dateFrom, Date dateTo){
        if (this.dateTo.before(dateFrom) || this.dateFrom.after(dateTo)) {
            return false;
        }
        return true;
    }

    /**
     * @param dateFrom beginning of reservation
     * @param dateTo end of reservation
     * @return number of positions, which customer can reserve
     */
    public Pair<Integer, Integer> countAllFreePositions(Date dateFrom, Date dateTo){
        int tallCounter = 0;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        databaseHandler = Warehouse.getInstance().getDatabaseHandler();
        Set<Position> allPositions = new HashSet<>(databaseHandler.getAllPositions());
        Set<CustomerReservation> allReservations = new HashSet<>(databaseHandler.getAllReservaions());
        for(CustomerReservation customerReservation : allReservations){
            if(overlapDate(customerReservation.getReservedFrom(), customerReservation.getReservedUntil())){
                allPositions.remove(databaseHandler.getPosition(customerReservation.getIdPosition()));
            }
        }
        for(Position p : allPositions){
            if(p.isTall()){
                tallCounter++;
            }
        }
        aviablePositions = allPositions;
        Warehouse.getInstance().addController("aviablePositions", aviablePositions);

        return new Pair<>(allPositions.size(), tallCounter);
    }

    class PositionComparator implements Comparator<Pair<Integer, List<Position>>>{
        @Override
        public int compare(Pair<Integer, List<Position>> res1, Pair<Integer, List<Position>> res2) {
            if(res1.getKey() < res2.getKey()){
                return 1;
            }
            if(res1.getKey() > res2.getKey()){
                return -1;
            }
            String firstPos = res1.getValue().get(0).getName();
            String secondPos = res2.getValue().get(0).getName();
            if(firstPos.compareTo(secondPos) > 0){
                return 1;
            }
            if(firstPos.compareTo(secondPos) < 0){
                return -1;
            }
            return 0;
        }
    }

    /**
     * Function, which choose best fit positions for reservation and save it to database in time interval
     * @param smallPositions number of basic/small positions
     * @param tallPositions number of tall positions
     * @param customerName whose customer reserves positions
     * @return flag which represents success of function
     */
    public boolean reservePositions(int smallPositions, int tallPositions, String customerName) {
        PriorityQueue<Pair<Integer, List<Position>>> priorityQueue = new PriorityQueue<>(new PositionComparator());
        PriorityQueue<Pair<Integer, List<Position>>> priorityQueueTallPositions = new PriorityQueue<>(new PositionComparator());
        int counter;
        boolean isTall = false;
        List<Position> addPositions;

        var positions = Warehouse.getInstance().getPositionsInGroups();
        for (var regals : positions.values()) {
            for(var row: regals.values()) {
                for(int index : row.keySet()){
                    counter = 0;
                    addPositions = new ArrayList<>();
                    List<Position> positionList = row.get(index);
                    for (Position position : positionList) {
                        if(aviablePositions.contains(position)){
                            addPositions.add(position);
                            counter++;
                            isTall = position.isTall();
                        }
                        else {
                            List<Customer> customerList = databaseHandler.getCustomerFromReservedPosition((java.sql.Date) dateFrom, (java.sql.Date) dateTo, position);
                            if (customerList != null) {
                                Customer c = customerList.get(0);
                                if (c.getName().equals(customerName)) {
                                    counter++;
                                    System.out.println(c.getName() + " " + position.getName());
                                }
                            }
                        }
                    }
                    Collections.sort(positionList, Comparator.comparing(Position::getName));
                    if(isTall){
                        priorityQueueTallPositions.add(new Pair<>(counter, addPositions));
                    }
                    else {
                        priorityQueue.add(new Pair<>(counter, addPositions));
                    }
                }
            }
        }
        List<Position> bestLowPositions = getBestFitPositions(smallPositions, priorityQueue);
        List<Position> bestTallPositions = getBestFitPositions(tallPositions, priorityQueueTallPositions);
        Warehouse.getInstance().addController("bestLowPositions", bestLowPositions);
        Warehouse.getInstance().addController("bestTallPositions", bestTallPositions);

        /*databaseHandler = Warehouse.getInstance().getDatabaseHandler();
        int customerId = databaseHandler.getCustomer(customerName).getId();
        if(! saveCustomerReservations(tallPositions, priorityQueueTallPositions, customerId)) {
            return false;
        }
        if(!saveCustomerReservations(smallPositions, priorityQueue, customerId)){
            return false;
        }*/

        return true;
    }
    protected List<Position> getBestFitPositions(int numberOfPorsitions, PriorityQueue<Pair<Integer, List<Position>>> priorityQueue){
        List<Position> bestPositions = new ArrayList<>();
        while (numberOfPorsitions > 0){
            var free = Objects.requireNonNull(priorityQueue.poll()).getValue();
            for(Position position : free) {
                if(numberOfPorsitions == 0){break;}
                bestPositions.add(position);
                numberOfPorsitions--;
            }
        }
        return bestPositions;
    }

    protected boolean saveCustomerReservations(int numberOfPorsitions, PriorityQueue<Pair<Integer, List<Position>>> priorityQueue, int customerId){
        while (numberOfPorsitions > 0){
            var free = Objects.requireNonNull(priorityQueue.poll()).getValue();
            for(Position position : free) {
                if(numberOfPorsitions == 0){break;}
                if(! databaseHandler.reservePositionForCustomer(customerId, (java.sql.Date) dateFrom, (java.sql.Date) dateTo, position)){
                    return false;
                }
                numberOfPorsitions--;
                System.out.println(numberOfPorsitions + " " + position.getName());
            }
        }
        return true;
    }

    public List<Map<String, String>> setReservationTable(Customer customer){
        List<CustomerReservation> records = Warehouse.getInstance().getDatabaseHandler().getReservationRecords(customer.getId());
        Map<Pair<Date, Date>, List<String>> sortedRecords = new HashMap<>();
        for(CustomerReservation record : records){
            Pair pair = new Pair(record.getReservedFrom(), record.getReservedUntil());
            if(!sortedRecords.containsKey(pair)){
                sortedRecords.put(pair, new ArrayList<>());
            }
            List<String> temp = sortedRecords.get(pair);
            temp.add(record.getIdPosition());
            sortedRecords.put(pair, temp);
        }
        List<Map<String,String>> result = new ArrayList<>();
        for (Pair date : sortedRecords.keySet()) {
            int numberOfPositions = sortedRecords.get(date).size();
            result.add(Map.of(
                    "Od", String.valueOf(date.getKey()),
                    "Do", String.valueOf(date.getValue()),
                    "Počet pozícií", String.valueOf(numberOfPositions)
            ));
        }

        return result;
    }

    public static void main(String[] args) {
        Reservation reservation = new Reservation();
        //reservation.reservePositions();
        System.out.println("A0475".compareTo("A0455"));
    }

}
