package app;

import Entity.*;
import javafx.util.Pair;

import java.util.*;

public class Reservation {
    Date dateFrom;
    Date dateTo;

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
        DatabaseHandler databaseHandler = Warehouse.getInstance().getDatabaseHandler();
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

        return new Pair<>(allPositions.size(), tallCounter);
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
        Date from = new Date(2024, 1, 13);
        Date to = new Date(2024, 1, 14);
        reservation.countAllFreePositions(from, to);
        //System.out.println(reservation.betweenDate(new Date(2024, 1, 13)));

    }

}
