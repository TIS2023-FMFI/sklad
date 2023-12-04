package app;

import Entity.Customer;
import Entity.DatabaseHandler;
import Entity.StoredOnPosition;
import javafx.scene.chart.BarChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Pair;

import java.sql.Date;
import java.util.Map;

public class Statistics {
    /***
     * This method takes data from database and fills the chart with them.
     * @param barChart is the chart that will be filled with data.
     */
    public void setBarChart(BarChart<?, ?> barChart, Date dateFrom, Date dateTo, String customerName) {
        Map<Date, Pair<Integer,Integer>> data = DatabaseHandler.getStatistics(dateFrom, dateTo, customerName);
    }

    /***
     * This method takes data from database and fills in the table used for inventory check.
     * @param inventoryTable is the table that will be filled with data.
     */
    public void setInventoryTable(TableView<StoredOnPosition> inventoryTable){
        inventoryTable.getItems().add(new StoredOnPosition(1, "00001", 8, 4));
    }

}
