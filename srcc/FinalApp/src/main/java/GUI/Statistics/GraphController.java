package GUI.Statistics;

import app.Statistics;
import app.Warehouse;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
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
        StatisticMainPageController input = (StatisticMainPageController) Warehouse.getInstance().getController("statisticsMainPage");
        Date dateFrom = input.dateFromValue;
        Date dateTo = input.dateToValue;
        String customer = input.customer.getValue();

        Statistics statistics = new Statistics();

        barChart.setTitle("Počet exportovaných a importovaných paliet");

        List<XYChart.Series<String, Number>> series = statistics.setBarChart(dateFrom, dateTo, customer);
        barChart.getData().addAll(series);
    }
    public void backToStatistics() throws IOException {
        Warehouse.getInstance().changeScene("Statistics/statisticsMainPageForm.fxml");
    }
}
