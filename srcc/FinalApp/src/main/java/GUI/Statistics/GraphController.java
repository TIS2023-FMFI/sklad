package GUI.Statistics;

import app.Statistics;
import app.Warehouse;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class GraphController implements Initializable {
    @FXML
    private BarChart<String,Number> barChart;

    /***
     * This method is called when the page is opened. It fills the chart with data.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        StatisticMainPageContoller input = (StatisticMainPageContoller) Warehouse.getInstance().getController("statisticsMainPage");

        Statistics statistics = new Statistics();

        barChart.setTitle("Počet exportovaných a importovaných paliet");

        List<XYChart.Series<String, Number>> series = statistics.setBarChart(null, null, null);
        barChart.getData().addAll(series);
    }
    public void backToStatistics() throws IOException {
        Warehouse.getInstance().changeScene("Statistics/statisticsMainPageForm.fxml");
    }
}
