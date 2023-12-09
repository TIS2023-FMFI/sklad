package GUI.Statistics;

import Entity.StoredOnPosition;
import app.Statistics;
import app.Warehouse;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InventoryConroller implements javafx.fxml.Initializable{

    @FXML
    public TableView<StoredOnPosition> inventoryTable;

    /***
     * This method is called when the page is opened. It fills the table with data.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Statistics statistics = new Statistics();
        inventoryTable.getItems().addAll(statistics.setInventoryTable());
    }

    public void saveInventoryList(){
    }

    public void backToStatistics() throws IOException {
        Warehouse.getInstance().changeScene("Statistics/statisticsMainPageForm.fxml");
    }
}
