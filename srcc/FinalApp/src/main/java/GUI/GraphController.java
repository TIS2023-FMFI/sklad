package GUI;

import app.Statistics;
import app.Warehouse;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GraphController implements javafx.fxml.Initializable{
    @FXML
    private BarChart<?,?> barChart;

    /***
     * This method is called when the page is opened. It fills the chart with data.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Statistics statistics = new Statistics();
        statistics.setBarChart(barChart, null, null, null);

    }
    public void backToStatistics() throws IOException {
        Warehouse.getInstance().changeScene("statisticsMainPage.fxml");
    }
}
