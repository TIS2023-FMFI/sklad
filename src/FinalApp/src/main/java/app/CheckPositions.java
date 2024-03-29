package app;

import Entity.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class CheckPositions implements Initializable {
    @FXML
    private TableView wrongPositionsTable;
    private static final String STYLE = "-fx-font: 17px 'Calibri'; -fx-alignment: CENTER;";
    @FXML
    private Label downloadConfirmationLabel;

    private Set<String> wrongPositions = new HashSet<>();
    private Set<String> palletToRemove = new HashSet<>();
    private ObservableList<Map<String, Object>> items = FXCollections.observableArrayList();

    /***
     * Creates new window with table of positions that should be empty but aren't.
     */
    public static void createNewWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(CheckPositions.class.getResource("RelocateProduct/checkPositions.fxml"));
            Parent root = loader.load();

            Stage newStage = new Stage();
            newStage.setTitle("Chybné pozície");
            newStage.setScene(new Scene(root));

            newStage.setOnCloseRequest(windowEvent -> {
                    Warehouse.getInstance().removeController("wrongPositions");}
                    );
            newStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * Fills table with wrong positions.
     * @param url The URL for the initialization.
     * @param resourceBundle The resource bundle associated with the initialization.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TableColumn<Map, String> PNRColumn = new TableColumn<>("Referencia");
        PNRColumn.setCellValueFactory(new MapValueFactory<>("Paleta"));
        PNRColumn.setStyle(STYLE);
        PNRColumn.setPrefWidth(200);
        TableColumn<Map, String> positionColumn = new TableColumn<>("Pozícia");
        positionColumn.setCellValueFactory(new MapValueFactory<>("Pozícia"));
        positionColumn.setStyle(STYLE);
        positionColumn.setPrefWidth(122);
        TableColumn<Map, String> materialColumn = new TableColumn<>("Materiál");
        materialColumn.setCellValueFactory(new MapValueFactory<>("Materiál"));
        materialColumn.setStyle(STYLE);
        materialColumn.setPrefWidth(122);
        TableColumn<Map, String> numberColumn = new TableColumn<>("Počet");
        numberColumn.setCellValueFactory(new MapValueFactory<>("Počet"));
        numberColumn.setStyle(STYLE);
        numberColumn.setPrefWidth(70);

        wrongPositions = (Set<String>) Warehouse.getInstance().getController("wrongPositions");
        System.out.println(wrongPositions);

        wrongPositionsTable.getColumns().addAll(positionColumn, PNRColumn, materialColumn, numberColumn);
        items.addAll(fillTable());
        wrongPositionsTable.getItems().addAll(items);

        deleteRecords();
        System.out.println(allPositionsCorrect());
    }
    /***
     * Saves a list of pallets that should be removed from the warehouse.
     */
    public boolean allPositionsCorrect(){
        wrongPositions = findWrongPositions();
        Warehouse.getInstance().addController("wrongPositions", wrongPositions);
        return wrongPositions.isEmpty();
    }
    private Set<String> findWrongPositions(){
        List<Customer> customers = Warehouse.getInstance().getDatabaseHandler().getCustomers();
        Set<String> allReservedPositions = new HashSet<>();
        for(Customer c : customers){
            allReservedPositions.addAll(getCustomerReservedPositions(c));
        }
        Set<String> usedPosition = new HashSet<>(Warehouse.getInstance().getDatabaseHandler().getAllUsedPositions());

        usedPosition.removeAll(allReservedPositions);
        return usedPosition;
    }
    private Set<String> getCustomerReservedPositions(Customer customer){
        List<CustomerReservation> customerReservations = Warehouse.getInstance().getDatabaseHandler().getReservationRecords(customer.getId());
        LocalDate localDate = LocalDate.now();
        Date today = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Set<String> currentReservedPositions = new HashSet<>();
        for(CustomerReservation reservation : customerReservations){
            if(overlapDate(reservation.getReservedFrom(), reservation.getReservedUntil(), today)){
                String add = reservation.getIdPosition();
                currentReservedPositions.add(add);
            }
        }
        return currentReservedPositions;
    }

    /***
     * Checks if date is between two other dates.
     * @param dateFrom start date
     * @param dateTo end date
     * @param current date to check
     * @return true if current date is between dateFrom and dateTo
     */
    public boolean overlapDate(Date dateFrom, Date dateTo, Date current){
        return (current.after(dateFrom) || current.equals(dateFrom)) && (current.before(dateTo) || current.equals(dateTo));
    }

    private List<Map<String, Object>> fillTable(){
        DatabaseHandler databaseHandler = Warehouse.getInstance().getDatabaseHandler();
        List<Map<String,Object>> result = new ArrayList<>();
        Set<String> usedPallets = new HashSet<>();
        for (String position : wrongPositions) {
            List<Pallet> palsForPos = databaseHandler.getPalletesOnPosition(position);
            for (Pallet p : palsForPos) {
                if (usedPallets.contains(p.getPnr())) {
                    continue;
                }
                usedPallets.add(p.getPnr());
                Position pos = databaseHandler.getPosition(position);
                Map<Material, Integer> products = Warehouse.getInstance().getPalletsOnPositionMap().get(pos).get(p);
                if (products == null) {
                    continue;
                }

                String positionString = getPositionsString(p);
                for (Map.Entry<Material, Integer> entry : products.entrySet()) {
                    result.add(Map.of(
                            "Paleta", p.getPnr(),
                            "Pozícia", positionString,
                            "Materiál", entry.getKey().getName(),
                            "Počet", String.valueOf(entry.getValue())));
                }
            }
        }
        palletToRemove = usedPallets;
        return result;
    }

    private String getPositionsString(Pallet p){
        StringBuilder result = new StringBuilder();
        List<Position> poses = Warehouse.getInstance().getDatabaseHandler().getPositionsOfPallet(p.getPnr());
        for (Position pos : poses) {
            result.append(pos.getName()).append("-");
        }
        return result.substring(0, result.length() - 1);
    }

    private void deleteRecords(){
        Warehouse warehouse = Warehouse.getInstance();
        DatabaseHandler databaseHandler = warehouse.getDatabaseHandler();

        for (String pnr : palletToRemove){
            Pallet pallet = databaseHandler.getPallet(pnr);
            List<Position> positions = databaseHandler.getPositionsOfPallet(pnr);
            for (Position position1 : positions){
                warehouse.removePalletOnPosition(position1, pallet);
            }
        }
        databaseHandler.deletePallets(palletToRemove);
    }


    /***
     * Exports table to Excel file.
     */
    public void exportExcel() {
        FileExporter fe = new FileExporter();
        fe.exportExcel(wrongPositionsTable.getItems(), "Vymazané produkty",
                "Zoznam", new ArrayList<>(Arrays.asList("Paleta", "Pozícia", "Materiál", "Počet")));
        downloadConfirmationLabel.setText("Súbor bol úspešne stiahnutý");
    }
}
