package GUI.Reservations;

import Entity.*;
import GUI.WarehouseLayout.RowLayoutController;
import app.Reservation;
import app.Warehouse;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;

import java.util.*;

public class RowLayoutReservationsController extends RowLayoutController implements Initializable {

    private static final String BLUE_COLOR = "#000080";    // rezervavanie aktualnym zakaznikom
    @FXML
    Label selectedPosition;
    @FXML
    Label errorMessage;
    Warehouse warehouse;
    Customer customer;
    Date dateFrom;
    Date dateTo;
    Set<Position> aviablePositions;
    Set<Position> positionsToSave;
    int numberOfPosition;
    int numberOfTallPosition;
    private static final String REMOVE_FROM_ADDED = "Pozícia bola odstránená z pridaných na zarezervovanie.";
    private static final String ADD_TO_ADDED = "Pozícia zvolená na uloženie.";
    private static final String ENOUGH_CHOOSEN_POSITIONS = "Nemožno pridať ďaľšiu pozíciu";
    private static final String RESERVED_BY_SAME_CUSTOMER = "Nemožno rezervovať pozíciu. Pozn: Pozícia je rezervoná zákazníkom v danom intervale.";
    private static final String CANNOT_SAVE = "Nemožno rezervovať zvolenú pozíciu.";
    private static final String NOT_ENOUGH_POSITIONS = "Málo zvolených pozícií.";
    private static final String DB_FAIL = "Chyba pri nahrávaní do DB";
    private static final String MORE_TALL_POSITIONS_1 = "Musíte ešte vybrať aspoň ";
    private static final String MORE_TALL_POSITIONS_2 = " vysokých pozícií.";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        warehouse = Warehouse.getInstance();

        positionsVBox.setAlignment(Pos.TOP_CENTER);
        palletsHBox.setAlignment(Pos.CENTER);
        palletsHBox.setSpacing(10);

        informationContainer1.setAlignment(Pos.TOP_LEFT);
        informationContainer2.setAlignment(Pos.TOP_LEFT);

        informationContainer1.setSpacing(20);
        informationContainer2.setSpacing(20);

        String nameCustomer = ((ChoiceBox<String>)Warehouse.getInstance().getController("customerReservationName")).getValue();
        customer = Warehouse.getInstance().getDatabaseHandler().getCustomer(nameCustomer);

        positionsToSave = (Set<Position>) warehouse.getController("positionsToSave");
        dateFrom =(Date) warehouse.getController("dateFrom");
        dateTo = (Date) warehouse.getController("dateTo");
        aviablePositions = (Set<Position>) warehouse.getController("aviablePositions");
        numberOfPosition = (Integer) warehouse.getController("lowPositions") + (Integer) warehouse.getController("tallPositions");
        numberOfTallPosition = (Integer) warehouse.getController("tallPositions");
        updateCounter();
        createGrid();
    }

    @Override
    protected Button createPositionButton(Position position) {
        Button positionButton = new Button();
        positionButton.setPrefWidth(POSITION_BUTTON_WIDTH);
        positionButton.setPrefHeight(position.isTall() ? TALL_POSITION_BUTTON_HEIGHT : POSITION_BUTTON_HEIGHT);
        positionButton.setStyle("-fx-background-color:" + getColor(position) + ";"
                + "-fx-border-color: black;"
                + "-fx-border-width: 1;");
        return positionButton;
    }


    @Override
    protected String getColor(Position position) {
        if(positionsToSave.contains(position)){  //odporucava pozicia
            return ORANGE_COLOR;
        }
        if(aviablePositions.contains(position)){  //volna pozicia
            return GREEN_COLOR;
        }
        if(warehouse.getDatabaseHandler().isPositionReserevedByCustomer((java.sql.Date) dateFrom, (java.sql.Date) dateTo, position, customer)){
            return BLUE_COLOR;
        }
        return RED_COLOR;
    }

    @Override
    public HBox createShelf(List<Position> positions) {
        HBox shelf = new HBox();
        shelf.setSpacing(POSITION_BUTTON_SPACING);

        for (Position position : positions) {
            Button positionButton = createPositionButton(position);
            shelf.getChildren().add(positionButton);

            positionButton.setOnMouseEntered(event -> positionName.setText(position.getName()));
            positionButton.setOnMouseExited(event -> positionName.setText(""));

            positionButton.setOnAction(event ->{ if(changeStatus(position)){
                positionButton.setStyle("-fx-background-color:" + getColor(position) + ";"
                        + "-fx-border-color: black;"
                        + "-fx-border-width: 1;");
            updateCounter();};});

            // positions where no pallets can be placed because there is a door
            if (position.getName().equals("A0331")) {
                addDoorPlaceholders(shelf, 4);
            }
            else if (position.getName().equals("A0330")) {
                addDoorPlaceholders(shelf, 2);
            }
        }
        return shelf;
    }
    private void updateCounter(){
        selectedPosition.setText(String.valueOf(numberOfPosition - positionsToSave.size()));
    }


    private boolean changeStatus(Position position){
        if(positionsToSave.contains(position)){
            errorMessage.setText(REMOVE_FROM_ADDED);
            positionsToSave.remove(position);
            return true;
        }

        if(aviablePositions.contains(position)){  //volna pozicia
            Pair<Integer, Integer> counter = new Reservation().getNumberOfLowTallPosition(positionsToSave);
            int low = counter.getKey();
            int tall = counter.getValue();
            int remainingPosition = numberOfPosition - low - tall;
            if(! position.isTall()){
                low += 1;
                remainingPosition = numberOfPosition - low - tall;
                if(remainingPosition == 0 && numberOfTallPosition - tall > 0){
                    errorMessage.setText(MORE_TALL_POSITIONS_1 + numberOfTallPosition + MORE_TALL_POSITIONS_2);
                    return false;
                }
            }
            if(numberOfPosition - positionsToSave.size() == 0) {
                errorMessage.setText(ENOUGH_CHOOSEN_POSITIONS);
                return false;
            }

            errorMessage.setText(ADD_TO_ADDED);
            positionsToSave.add(position);
            return true;
        }
        if(warehouse.getDatabaseHandler().isPositionReserevedByCustomer((java.sql.Date) dateFrom, (java.sql.Date) dateTo, position, customer)){
            errorMessage.setText(RESERVED_BY_SAME_CUSTOMER);
            return false;
        }
        errorMessage.setText(CANNOT_SAVE);
        return false;
    }
    @Override
    public void backToRows() throws IOException {
        Warehouse.getInstance().removeController("rowLayout");
        Warehouse.getInstance().changeScene("Reservations/warehouseLayoutRowsReservationForm.fxml");
    }


    public void reserve() throws IOException {
        if(numberOfPosition - positionsToSave.size() > 0){
            errorMessage.setText(NOT_ENOUGH_POSITIONS);
            return;
        }
        Reservation reservation = new Reservation();
        if(reservation.saveCustomerReservations(positionsToSave, customer, dateFrom, dateTo)) {
            Warehouse.getInstance().changeScene("Reservations/reservationConfirmation.fxml");
            return;
        }
        errorMessage.setText(DB_FAIL);

    }
}
