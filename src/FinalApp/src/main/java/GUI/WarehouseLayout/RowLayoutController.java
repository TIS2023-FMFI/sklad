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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.*;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class RowLayoutController implements Initializable {
    @FXML
    private VBox positionsVBox;

    @FXML
    private Label positionName;

    @FXML
    private HBox palletsHBox;

    @FXML
    private VBox informationContainer1;

    @FXML
    private VBox informationContainer2;

    @FXML
    private AnchorPane materialCountTable;

    private static final String RED_COLOR = "#CF1616";
    private static final String ORANGE_COLOR = "#FF8D00";
    private static final String GREEN_COLOR = "#008000";

    private static final int POSITION_BUTTON_WIDTH = 35;
    private static final int POSITION_BUTTON_HEIGHT = 35;
    private static final int TALL_POSITION_BUTTON_HEIGHT = 60;
    private static final int POSITION_BUTTON_SPACING = 3;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Warehouse.getInstance().addController("rowLayout", this);

        Warehouse.getStage().setMinWidth(1300);
        Warehouse.getStage().setMinHeight(700);

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

    private Button createPositionButton(Position position) {
        Button positionButton = new Button();
        positionButton.setPrefWidth(POSITION_BUTTON_WIDTH);
        positionButton.setPrefHeight(position.isTall() ? TALL_POSITION_BUTTON_HEIGHT : POSITION_BUTTON_HEIGHT);
        positionButton.setStyle("-fx-background-color:" + getColor(position) + ";"
                + "-fx-border-color: black;"
                + "-fx-border-width: 1;");
        return positionButton;
    }

    private void addDoorPlaceholders(HBox shelf, int count) {
        for (int i = 0; i < count; i++) {
            Region doorPlaceholder = createDoorPlaceholder();
            shelf.getChildren().add(doorPlaceholder);
        }
    }

    private Region createDoorPlaceholder() {
        Region doorPlaceholder = new Region();
        doorPlaceholder.setPrefSize(POSITION_BUTTON_WIDTH, POSITION_BUTTON_HEIGHT);
        return doorPlaceholder;
    }

    private String getColor(Position position) {
        Warehouse warehouse = Warehouse.getInstance();
        if (!warehouse.isPalletOnPosition(position)) {
            return RED_COLOR;
        }
        else if (warehouse.getDatabaseHandler().isPositionReserved(position.getName())) {
            return ORANGE_COLOR;
        }
        return GREEN_COLOR;
    }

    private void handlePositionButtonClick(Position position) {
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

    public void positionWithPallets(Position position, Map<Pallet, Map<Material, Integer>> palletsOnPosition){
        int count = 1;
        for (Pallet pallet : palletsOnPosition.keySet()){
            Button palletButton = new Button("Paleta-" + count);
            palletsHBox.getChildren().add(palletButton);
            palletButton.setOnAction(event -> handlePalletButtonClick(position, pallet, palletsOnPosition.get(pallet)));
            count++;
        }
    }

    private void handlePalletButtonClick(Position position, Pallet pallet, Map<Material, Integer> materialsAndCount) {
        clearInformationContainers();
        DatabaseHandler databaseHandler = Warehouse.getInstance().getDatabaseHandler();

        Label positionName = new Label("Názov pozície: " + position.getName());
        Label customer = new Label("Zakázník: " + databaseHandler.getCustomerThatReservedPosition(position).getName());
        Label palletType = new Label("Typ palety: " + pallet.getType());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String dateSK = dateFormat.format(pallet.getDateIncome());
        Label date = new Label("Dátum zaskladnenia: " + dateSK);
        Label isTall = new Label("Nadrozmernosť: " + (position.isTall() ? "áno" : "nie"));

        informationContainer1.getChildren().addAll(positionName, customer, palletType, date, isTall);

        Label weight = new Label("Hmotnosť: " + pallet.getWeight());
        Label user = new Label("Meno skladníka: " + databaseHandler.getUsername(pallet.getIdUser()));
        Label isDamaged = new Label("Poškodenosť: " + (pallet.isDamaged() ? "áno" : "nie"));
        Label note = new Label("Poznámka: " + pallet.getNote());

        informationContainer2.getChildren().addAll(weight, user, isDamaged, note);

        TableView<Map.Entry<Material, Integer>> table = new TableView<>();
        TableColumn<Map.Entry<Material, Integer>, String> materialColumn = new TableColumn<>("Material");
        TableColumn<Map.Entry<Material, Integer>, Integer> countColumn = new TableColumn<>("Count");

        materialColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getKey().getName()));
        countColumn.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getValue()).asObject());

        materialColumn.setMinWidth(170);
        countColumn.setMinWidth(50);
        table.getColumns().addAll(materialColumn, countColumn);

        table.setPrefWidth(225);
        table.setMaxHeight(170);

        for (Map.Entry<Material, Integer> entry : materialsAndCount.entrySet()) {
            table.getItems().add(entry);
        }
        materialCountTable.getChildren().add(table);
    }


    public void reservedPosition(Position position){
        DatabaseHandler databaseHandler = Warehouse.getInstance().getDatabaseHandler();
        CustomerReservation reservation = databaseHandler.getReservation(position.getName());

        Label positionName = new Label("Názov pozície: " + position.getName());
        Label reservedFor = new Label("Rezervované pre: " + databaseHandler.getCustomerThatReservedPosition(position).getName());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String dateFrom = dateFormat.format(reservation.getReservedFrom());
        String dateUntil = dateFormat.format(reservation.getReservedUntil());
        Label reservationDate = new Label("Dátum rezervácie: " + dateFrom + "-" + dateUntil);

        informationContainer1.getChildren().addAll(positionName, reservedFor, reservationDate);
    }

    public void freePosition(){
        Label freePosition = new Label("Zvolená pozícia je voľná");
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
