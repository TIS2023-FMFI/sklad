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

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class RowLayoutReservationsController extends RowLayoutController implements Initializable {

    private static final String BLUE_COLOR = "#000080";    // rezervavanie aktualnym zakaznikom
    @FXML
    Label selectedPosition;
    @FXML
    Label errorMessage;
    @FXML
    Button reserve;
    Warehouse warehouse;
    Customer customer;
    Date dateFrom;
    Date dateTo;
    Set<Position> aviablePositions;
    Set<Position> positionsToSave;
    int numberOfPosition;
    private final String REMOVE_FROM_ADDED = "Pozícia bola odstránená z pridaných na zarezervovanie.";
    private final String ADD_TO_ADDED = "Pozícia zvolená na uloženie.";
    private final String ENOUGH_CHOOSEN_POSITIONS = "Nemôžno pridať ďaľšiu pozíciu";
    private final String RESERVED_BY_SAME_CUSTOMER = "Nemôžno rezervovať pozíciu. Pozn: Pozícia je rezervoná zákazníkom v danom intervale.";
    private final String CANNOT_SAVE = "Nemôžno rezervovať zvolenú pozíciu.";
    private final String NOT_ENOUGH_POSITIONS = "Málo zvolených pozícií.";
    private final String DB_FAIL = "Chyba pri nahrávaní do DB";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        warehouse = Warehouse.getInstance();

        Warehouse.getStage().setMinWidth(1300);
        Warehouse.getStage().setMinHeight(700);

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
        numberOfPosition = (Integer) warehouse.getController("numberOfPosition");

        updateCounter();
//        System.out.println(positionsToSave.size() + " " + numberOfPosition);
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
    protected void handlePositionButtonClick(Position position) {
        Warehouse warehouse = Warehouse.getInstance();
        Map<Pallet, Map<Material, Integer>> palletsOnPosition = warehouse.getPalletsOnPosition(position);

        palletsHBox.getChildren().clear();
        clearInformationContainers();

        if (!palletsOnPosition.isEmpty()) {
            positionWithPallets(position, palletsOnPosition);
        }
        else if (warehouse.getDatabaseHandler().isPositionReserved(position.getName())) {
            reservedPosition(position);
        }
        else {
            freePosition();
        }
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
            Warehouse.getInstance().changeScene("Reservations/createReservationConfirmation.fxml");
            return;
        }
        errorMessage.setText(DB_FAIL);

    }
}