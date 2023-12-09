package app;

import Entity.DatabaseHandler;
import Entity.StoredOnPosition;
import jakarta.persistence.criteria.CriteriaBuilder;
import javafx.collections.FXCollections;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Pair;

import java.sql.Array;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Statistics {
    /***
     * This method takes data from database and fills the chart with them.
     * @param dateFrom Date from which the data should be taken.
     * @param dateTo Date to which the data should be taken.
     * @param customerName Name of the customer whose data should be taken.
     * @return BarChart with data.
     */
    public List<XYChart.Series<String,Number>> setBarChart(Date dateFrom, Date dateTo, String customerName) {
        DatabaseHandler db = Warehouse.getInstance().getDatabaseHandler();
        Map<Date, Pair<Integer,Integer>> data = db.getStatistics(dateFrom, dateTo, customerName);

        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("Export");

        series1.getData().add(new XYChart.Data<>("1.12.2023", 1));
        series1.getData().add(new XYChart.Data<>("2.12.2023", 3));
        series1.getData().add(new XYChart.Data<>("3.12.2023", 5));
        series1.getData().add(new XYChart.Data<>("4.12.2023", 5));

        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series2.setName("Import");
        series2.getData().add(new XYChart.Data<>("1.12.2023", 5));
        series2.getData().add(new XYChart.Data<>("2.12.2023", 6));
        series2.getData().add(new XYChart.Data<>("3.12.2023", 10));
        series2.getData().add(new XYChart.Data<>("4.12.2023", 4));

        return Arrays.asList(
                series1,
                series2
        );
    }

    /***
     * This method takes data from database and fills in the table used for inventory check.
     */
    public List<StoredOnPosition> setInventoryTable(){
        StoredOnPosition storedOnPosition = new StoredOnPosition(1, "00001", 8, 4);

        return List.of(storedOnPosition);
    }

}
