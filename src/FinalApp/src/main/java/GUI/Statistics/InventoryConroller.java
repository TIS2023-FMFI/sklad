package GUI.Statistics;

import Entity.StoredOnPallet;
import app.FileExporter;
import app.Statistics;
import app.Warehouse;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class InventoryConroller implements Initializable {

    @FXML
    private TableView inventoryTable;

    /***
     * This variable is used to store the data for the inventory table.
     */
    private ObservableList<Map<String, String>> items = FXCollections.observableArrayList();

    /***
     * This method is called when the page is opened. It fills the table with data.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Statistics statistics = new Statistics();

        TableColumn<Map, String> positionColumn = new TableColumn<>("Pozícia");
        positionColumn.setCellValueFactory(new MapValueFactory<>("Pozícia"));

        TableColumn<Map, String> PNRColumn = new TableColumn<>("PNR");
        PNRColumn.setCellValueFactory(new MapValueFactory<>("PNR"));

        TableColumn<Map, String> materialColumn = new TableColumn<>("Materiál");
        materialColumn.setCellValueFactory(new MapValueFactory<>("Materiál"));

        TableColumn<Map, String> quantityColumn = new TableColumn<>("Počet");
        quantityColumn.setCellValueFactory(new MapValueFactory<>("Počet"));

        inventoryTable.getColumns().addAll(positionColumn, PNRColumn, materialColumn, quantityColumn);

        items.addAll(statistics.setInventoryTable());

        inventoryTable.getItems().addAll(items);
    }

    /***
     * This method is called when the user clicks on the "Export" button. It exports the table to an excel file.
     */
    public void saveInventoryList(){
        FileExporter fileExporter = new FileExporter();
        List<String> columns = new ArrayList<>();
        columns.add("Pozícia");
        columns.add("PNR");
        columns.add("Materiál");
        columns.add("Počet");
        fileExporter.exportExcel(items, "Inventory", "Inventúra " + LocalDate.now(), columns);
    }

    /***
     * This method is called when the user clicks on the "Back" button. It returns the user to the statistics main page.
     * @throws IOException
     */
    public void backToStatistics() throws IOException {
        Warehouse.getInstance().changeScene("Statistics/statisticsMainPageForm.fxml");
    }
}
