package GUI;

import app.Statistics;
import app.Warehouse;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class GraphController implements javafx.fxml.Initializable{
    @FXML
    private BarChart<String,Number> barChart;

    /***
     * This method is called when the page is opened. It fills the chart with data.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Statistics statistics = new Statistics();

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setCategories(FXCollections.<String>
                observableArrayList(Arrays.asList("1.12.2023", "2.12.2023", "3.12.2023", "4.12.2023")));
        xAxis.setLabel("Dátum");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Počet");

        barChart.setTitle("Počet exportovaných a importovaných paliet");

        List<XYChart.Series<String, Number>> series = statistics.setBarChart(null, null, null);
        barChart.getData().addAll(series);
    }
    public void backToStatistics() throws IOException {
        Warehouse.getInstance().changeScene("statisticsMainPage.fxml");
    }
}
