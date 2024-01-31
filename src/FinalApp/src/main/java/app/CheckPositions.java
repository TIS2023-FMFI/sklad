package app;

import Entity.*;
import jakarta.persistence.Table;
import jakarta.persistence.criteria.CriteriaBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CheckPositions implements Initializable {
    @FXML
    public TableView wrongPositionsTable;
    @FXML
    public Label label;

    private final String STYLE = "-fx-font: 17px 'Calibri'; -fx-alignment: CENTER;";
    private final String ALL_POSITION_OK = "Všetky pozície sú v poriadku.";
    private final String WRONG_POSITIONS = "Na niektorých pozíciách je tovar napriek, no nie sú\nrezervované žiadnym zákazníkom.\n" +
            "Premiestnite tovar z:";

    private Set<String> wrongPositions = new HashSet<>();
    private ObservableList<Map<String, Object>> items = FXCollections.observableArrayList();

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
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TableColumn<Map, String> PNRColumn = new TableColumn<>("PNR");
        PNRColumn.setCellValueFactory(new MapValueFactory<>("pnr"));
        PNRColumn.setStyle(STYLE);
        PNRColumn.setPrefWidth(120);
        TableColumn<Map, String> positionColumn = new TableColumn<>("Pozícia");
        positionColumn.setCellValueFactory(new MapValueFactory<>("position"));
        positionColumn.setStyle(STYLE);
        positionColumn.setPrefWidth(120);
        TableColumn<Map, String> materialColumn = new TableColumn<>("Materiál");
        materialColumn.setCellValueFactory(new MapValueFactory<>("material"));
        materialColumn.setStyle(STYLE);
        materialColumn.setPrefWidth(120);
        TableColumn<Map, String> numberColumn = new TableColumn<>("Počet");
        numberColumn.setCellValueFactory(new MapValueFactory<>("number"));
        numberColumn.setStyle(STYLE);
        numberColumn.setPrefWidth(120);

        wrongPositions = (Set<String>) Warehouse.getInstance().getController("wrongPositions");

        wrongPositionsTable.getColumns().addAll(positionColumn, PNRColumn, materialColumn, numberColumn);
        items.addAll(fillTable());
        wrongPositionsTable.getItems().addAll(items);
    }
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

    public boolean overlapDate(Date dateFrom, Date dateTo, Date current){
        return (current.after(dateFrom) || current.equals(dateFrom)) && (current.before(dateTo) || current.equals(dateTo));
    }

    private List<Map<String, Object>> fillTable(){
        DatabaseHandler databaseHandler = Warehouse.getInstance().getDatabaseHandler();
        List<Map<String,Object>> result = new ArrayList<>();
        System.out.println(wrongPositions);
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
                            "pnr", p.getPnr(),
                            "position", positionString,
                            "material", entry.getKey().getName(),
                            "number", entry.getValue()));
                }
            }


        }
        return result;
    }

    String getPositionsString(Pallet p){
        StringBuilder result = new StringBuilder();
        List<Position> poses = Warehouse.getInstance().getDatabaseHandler().getPositionsOfPallet(p.getPnr());
        for (Position pos : poses) {
            result.append(pos.getName()).append("-");
        }
        return result.substring(0, result.length() - 1);
    }


}
