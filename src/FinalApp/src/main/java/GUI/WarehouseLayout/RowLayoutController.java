package GUI.WarehouseLayout;

import Entity.CustomerReservation;
import Entity.Material;
import Entity.Pallet;
import app.DatabaseHandler;
import app.Warehouse;
import Entity.Position;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXButton;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Callback;

public class RowLayoutController implements Initializable {
    @FXML
    protected VBox positionsVBox;

    @FXML
    protected Label positionName;

    @FXML
    protected HBox palletsHBox;

    @FXML
    protected VBox informationContainer1;

    @FXML
    protected VBox informationContainer2;

    @FXML
    protected AnchorPane materialCountTable;

    protected static final String RED_COLOR = "#CF1616";
    protected static final String ORANGE_COLOR = "#FF8D00";
    protected static final String GREEN_COLOR = "#008000";

    protected static final int POSITION_BUTTON_WIDTH = 40;
    protected static final int POSITION_BUTTON_HEIGHT = 50;
    protected static final int TALL_POSITION_BUTTON_HEIGHT = 75;
    protected static final int POSITION_BUTTON_SPACING = 3;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Warehouse.getInstance().addController("rowLayout", this);

        positionsVBox.setAlignment(Pos.TOP_CENTER);
        palletsHBox.setAlignment(Pos.CENTER);
        palletsHBox.setSpacing(10);

        informationContainer1.setAlignment(Pos.TOP_LEFT);
        informationContainer2.setAlignment(Pos.TOP_LEFT);

        informationContainer1.setSpacing(20);
        informationContainer2.setSpacing(20);

        createGrid();
    }

    public void createGrid() {
        Map<Integer, List<Position>> shelfAndItsPositions = Warehouse.getInstance().getWarehouseLayoutInstance().getShelfAndItsPositions();
        positionsVBox.setSpacing(POSITION_BUTTON_SPACING);

        for (List<Position> positions : shelfAndItsPositions.values()) {
            HBox shelf = createShelf(positions);
            shelf.setAlignment(Pos.CENTER);
            positionsVBox.getChildren().add(shelf);
        }
    }

    public HBox createShelf(List<Position> positions) {
        HBox shelf = new HBox();
        shelf.setSpacing(POSITION_BUTTON_SPACING);

        for (Position position : positions) {
            Button positionButton = createPositionButton(position);
            shelf.getChildren().add(positionButton);

            positionButton.setOnMouseEntered(event -> positionName.setText(position.getName()));
            positionButton.setOnMouseExited(event -> positionName.setText(""));

            positionButton.setOnAction(event -> handlePositionButtonClick(position));

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

    protected Button createPositionButton(Position position) {
        Button positionButton = new Button();
        positionButton.setPrefWidth(POSITION_BUTTON_WIDTH);
        positionButton.setPrefHeight(position.isTall() ? TALL_POSITION_BUTTON_HEIGHT : POSITION_BUTTON_HEIGHT);
        positionButton.setStyle("-fx-background-color:" + getColor(position) + ";"
                + "-fx-border-color: black;"
                + "-fx-border-width: 1;");
        return positionButton;
    }

    protected void addDoorPlaceholders(HBox shelf, int count) {
        for (int i = 0; i < count; i++) {
            Region doorPlaceholder = createDoorPlaceholder();
            shelf.getChildren().add(doorPlaceholder);
        }
    }

    protected Region createDoorPlaceholder() {
        Region doorPlaceholder = new Region();
        doorPlaceholder.setPrefSize(POSITION_BUTTON_WIDTH, POSITION_BUTTON_HEIGHT);
        return doorPlaceholder;
    }

    protected String getColor(Position position) {
        Warehouse warehouse = Warehouse.getInstance();
        if (!warehouse.isPalletOnPosition(position)) {
            return RED_COLOR;
        }
        else if (warehouse.getDatabaseHandler().isPositionReservedToday(position.getName())) {
            return ORANGE_COLOR;
        }
        return GREEN_COLOR;
    }

    protected void handlePositionButtonClick(Position position) {
        Warehouse warehouse = Warehouse.getInstance();
        Map<Pallet, Map<Material, Integer>> palletsOnPosition = warehouse.getPalletsOnPosition(position);

        palletsHBox.getChildren().clear();
        clearInformationContainers();

        if (!palletsOnPosition.isEmpty()) {
            positionWithPallets(position, palletsOnPosition);
        }
        else if (warehouse.getDatabaseHandler().isPositionReservedToday(position.getName())) {
            reservedPosition(position);
        }
        else {
            freePosition();
        }
    }

    public void positionWithPallets(Position position, Map<Pallet, Map<Material, Integer>> palletsOnPosition){
        for (Pallet pallet : palletsOnPosition.keySet()){
            JFXButton palletButton = Warehouse.getInstance().createStyledButton("Paleta-" + pallet.getPnr(), "#0C356A",
                    "#FFFFFF",  130, 45, 23, true);
            palletsHBox.getChildren().add(palletButton);
            palletButton.setOnAction(event -> handlePalletButtonClick(position, pallet, palletsOnPosition.get(pallet)));
        }
    }

    protected void handlePalletButtonClick(Position position, Pallet pallet, Map<Material, Integer> materialsAndCount) {
        clearInformationContainers();
        DatabaseHandler databaseHandler = Warehouse.getInstance().getDatabaseHandler();

        String allPositionNames = String.join(", ", databaseHandler.getPositionsWithPallet(pallet.getPnr()));
        Label positionName = createStyledLabel("Názov pozície: " + allPositionNames);
        Label customer = createStyledLabel("Zakázník: " + databaseHandler.getCustomerThatReservedPosition(position).getName());
        Label palletType = createStyledLabel("Typ palety: " + pallet.getType());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String dateSK = dateFormat.format(pallet.getDateIncome());
        Label date = createStyledLabel("Dátum zaskladnenia: " + dateSK);
        Label isTall = createStyledLabel("Nadrozmernosť: " + (position.isTall() ? "áno" : "nie"));

        informationContainer1.getChildren().addAll(positionName, customer, palletType, date, isTall);

        Label numberOfPosition = createStyledLabel("Počet pozícií: " + pallet.getNumberOfPositions());
        Label weight = createStyledLabel("Hmotnosť: " + pallet.getWeight() + " kg");
        Label user = createStyledLabel("Meno skladníka: " + databaseHandler.getUsername(pallet.getIdUser()));
        Label isDamaged = createStyledLabel("Poškodenosť: " + (pallet.isDamaged() ? "áno" : "nie"));
        Label note = createStyledLabel("Poznámka: " + pallet.getNote());

        informationContainer2.getChildren().addAll(numberOfPosition, weight, user, isDamaged, note);

        TableView<Map.Entry<Material, Integer>> table = new TableView<>();
        TableColumn<Map.Entry<Material, Integer>, String> materialColumn = new TableColumn<>("Materiál");
        TableColumn<Map.Entry<Material, Integer>, Integer> countColumn = new TableColumn<>("Počet");

        materialColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getKey().getName()));
        countColumn.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getValue()).asObject());

        materialColumn.setMinWidth(250);
        countColumn.setMinWidth(100);
        table.getColumns().addAll(materialColumn, countColumn);

        table.setPrefWidth(352);
        table.setMaxHeight(210);

        table.setStyle("-fx-font-family: Calibri; -fx-font-size: 17;");

        countColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Map.Entry<Material, Integer>, Integer> call(TableColumn<Map.Entry<Material, Integer>, Integer> param) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(item.toString());
                            setAlignment(javafx.geometry.Pos.CENTER);
                        }
                    }
                };
            }
        });

        for (Map.Entry<Material, Integer> entry : materialsAndCount.entrySet()) {
            table.getItems().add(entry);
        }
        materialCountTable.getChildren().add(table);
    }

    private Label createStyledLabel(String labelText) {
        Label label = new Label(labelText);

        label.setTextFill(Color.BLACK);
        label.setFont(Font.font("Calibri", FontWeight.NORMAL, 17));

        return label;
    }


    public void reservedPosition(Position position){
        DatabaseHandler databaseHandler = Warehouse.getInstance().getDatabaseHandler();
        CustomerReservation reservation = databaseHandler.getReservation(position.getName());

        Label positionName = createStyledLabel("Názov pozície: " + position.getName());
        Label reservedFor = createStyledLabel("Rezervované pre: " + databaseHandler.getCustomerThatReservedPosition(position).getName());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String dateFrom = dateFormat.format(reservation.getReservedFrom());
        String dateUntil = dateFormat.format(reservation.getReservedUntil());
        Label reservationDate = createStyledLabel("Dátum rezervácie: " + dateFrom + "-" + dateUntil);

        informationContainer1.getChildren().addAll(positionName, reservedFor, reservationDate);
    }

    public void freePosition(){
        Label freePosition = createStyledLabel("Zvolená pozícia je voľná");
        informationContainer1.getChildren().add(freePosition);
    }

    public void clearInformationContainers(){
        informationContainer1.getChildren().clear();
        informationContainer2.getChildren().clear();
        materialCountTable.getChildren().clear();
    }

    public void backToRows() throws IOException {
        Warehouse.getInstance().removeController("rowLayout");
        Warehouse.getInstance().changeScene("WarehouseLayout/warehouseLayoutRowsForm.fxml");
    }
}
