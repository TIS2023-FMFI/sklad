package app;

import Entity.*;
import javafx.util.Pair;

import java.util.*;

public class Reservation {
    class DatePair {
        private Date startDate;
        private Date endDate;

        public DatePair(Date startDate, Date endDate) {
            this.startDate = startDate;
            this.endDate = endDate;
        }
        public Date getStartDate() {
            return startDate;
        }
        public Date getEndDate() {
            return endDate;
        }

    }

    protected int getNumberOfFreePositions(Date dateFrom, Date dateTo){
        int counter = 0;
        return counter;
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

}
