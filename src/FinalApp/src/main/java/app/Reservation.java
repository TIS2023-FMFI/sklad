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
            return false;  // No intersection
        }
        return true;
    }

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

        return new Pair<>(allPositions.size(), tallCounter);
    }
    public boolean reservePositions(int smallPositions, int tallPositions, String customerName) {
        PriorityQueue<Pair<Integer, List<Position>>> priorityQueue = new PriorityQueue<>(Comparator.comparing(Pair :: getKey, Comparator.reverseOrder()));
        PriorityQueue<Pair<Integer, List<Position>>> priorityQueueTallPositions = new PriorityQueue<>(Comparator.comparing(Pair :: getKey, Comparator.reverseOrder()));
        int counter;
        boolean isTall = false;
        List<Position> addPositions;

//        var positions = Warehouse.getInstance().getDatabaseHandler().getPositionInGroups();
        var positions = Warehouse.getInstance().getPositionsInGroups();
        //Map<String, Map<Integer, Map<Integer, List<Position>>>>
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
                    }
                    if(isTall){
                        priorityQueueTallPositions.add(new Pair<>(counter, addPositions));
                    }
                    else {
                        priorityQueue.add(new Pair<>(counter, addPositions));
                    }
                }
            }
        }

        databaseHandler = Warehouse.getInstance().getDatabaseHandler();
        int customerId = databaseHandler.getCustomer(customerName).getId();
        while (tallPositions > 0){
            var free = Objects.requireNonNull(priorityQueueTallPositions.poll()).getValue();
            for(Position position : free) {
                if(tallPositions == 0){break;}
                if(! databaseHandler.reservePositionForCustomer(customerId, (java.sql.Date) dateFrom, (java.sql.Date) dateTo, position)){
                    return false;
                }
                tallPositions--;
            }
        }

        while (smallPositions > 0){
            var free = Objects.requireNonNull(priorityQueue.poll()).getValue();
            for(Position position : free) {
                if(smallPositions == 0){break;}
                if(! databaseHandler.reservePositionForCustomer(customerId, (java.sql.Date) dateFrom, (java.sql.Date) dateTo, position)){
                    return false;
                }
                smallPositions--;
                System.out.println(smallPositions + " " + position.getName());
            }
        }
        return true;
    }

    protected List<CustomerReservation> getCustomerReservation(int id){
        List<CustomerReservation> records = Warehouse.getInstance().getDatabaseHandler().getReservationRecords(id);
        for(var i : records){
            System.out.println(i);
        }
        return records;
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

    }

}
