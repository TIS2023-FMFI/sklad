package app;

import Entity.*;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.util.Pair;
import com.jfoenix.controls.JFXButton;

import java.io.IOException;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
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

        if(Warehouse.getInstance().getController("aviablePositions") != null){
            Warehouse.getInstance().removeController("aviablePositions");
        }
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
            if(res1.getValue().size() == 0 || res2.getValue().size() == 0 ){
                return 0;
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
    public boolean findPotisionsToReserve(int smallPositions, int tallPositions, String customerName) {
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

        if(Warehouse.getInstance().getController("bestLowPositions") != null){
            Warehouse.getInstance().removeController("bestLowPositions");
        }
        Warehouse.getInstance().addController("bestLowPositions", bestLowPositions);

        if(Warehouse.getInstance().getController("bestTallPositions") != null){
            Warehouse.getInstance().removeController("bestTallPositions");
        }
        Warehouse.getInstance().addController("bestTallPositions", bestTallPositions);
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

    /**
     * Function saves given position to dababase
     * @param positions positions to save
     * @param customer to whom customer reserve positions
     * @param dateFrom date interval from
     * @param dateTo date interval from
     * @return success of the operation
     */
    public boolean saveCustomerReservations(Set<Position> positions, Customer customer, Date dateFrom, Date dateTo){
        for(Position position : positions) {
            if(! Warehouse.getInstance().getDatabaseHandler().reservePositionForCustomer(customer.getId(), (java.sql.Date) dateFrom, (java.sql.Date) dateTo, position)){
                return false;
            }
        }
        return true;
    }
    private Button createDeleteButton( Map<Pair<Date, Date>, List<String>> sortedRecords, Pair date, Customer customer){
        JFXButton edit = new JFXButton("Zmazať");
        String style = "-fx-font: 17px 'Calibri'; -fx-alignment: CENTER; -fx-background-color: #B80F0A; -fx-font-weight: bold;";

        edit.setStyle(style);
        edit.setTextFill(Color.WHITE);
        if(! Warehouse.getInstance().getCurrentUser().getAdmin()){
            edit.setVisible(false);
        }
        edit.setOnAction(event ->{
            if(Warehouse.getInstance().getController("cannotRemove") != null){
                Warehouse.getInstance().removeController("cannotRemove");
            }
            if(Warehouse.getInstance().getController("DeletedReservation") != null){
                Warehouse.getInstance().removeController("DeletedReservation");
            }
            LocalDate parsedDate1 = LocalDate.parse(String.valueOf(date.getKey()));
            LocalDate today = LocalDate.now();
            if(!parsedDate1.isAfter(today)) {
                findUsedPositions(new HashSet<>(sortedRecords.get(date)));
            }
            if(Warehouse.getInstance().getController("cannotRemove") == null ||
                    ((Set<String>) Warehouse.getInstance().getController("cannotRemove")).isEmpty()) {
                Warehouse.getInstance().getDatabaseHandler().deleteReservationRecord(customer, (java.sql.Date) date.getKey(), (java.sql.Date) date.getValue());
                Warehouse.getInstance().addController("DeletedReservation", true);
            }
            try {
                Warehouse.getInstance().changeScene("Reservations/reservationConfirmation.fxml");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return edit;

    }

    /**
     * Function takes information from database about customer reservation of his positions
     * @param customer to whom customer find data
     * @return reservation records grouped by date intervals
     */
    public List<Map<String, Object>> setReservationTable(Customer customer){
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
        List<Map<String,Object>> result = new ArrayList<>();

        for (Pair date : sortedRecords.keySet()) {
            int numberOfPositions = sortedRecords.get(date).size();
            Button edit = createDeleteButton(sortedRecords, date, customer);
            LocalDate parsedDate1 = LocalDate.parse(String.valueOf(date.getKey()));
            LocalDate parsedDate2 = LocalDate.parse(String.valueOf(date.getValue()));
            LocalDate today = LocalDate.now();
            if(parsedDate2.isBefore(today)){
                continue;
            }

            result.add(Map.of(
                    "from", parsedDate1.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                    "until", parsedDate2.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                    "numberOfReservedPositions", numberOfPositions,
                    "edit", edit
            ));
        }
        return result;
    }


    private void findUsedPositions(HashSet<String> reservedPositions){
        Set<String> usedPosition = new HashSet<>(Warehouse.getInstance().getDatabaseHandler().getAllUsedPositions());
        reservedPositions.retainAll(usedPosition);
        Warehouse.getInstance().addController("cannotRemove", reservedPositions);
    }

    /**
     * returns sum of low positions and sum of tall positions
     * @param allPosition set of position
     * @return Pair of sum of low and tall positions
     */
    public Pair<Integer, Integer> getNumberOfLowTallPosition(Set<Position> allPosition){
        int low = 0;
        int tall = 0;
        for(Position p : allPosition){
            if(p.isTall()) tall++;
            else low++;
        }
        return new Pair<>(low, tall);
    }

}
