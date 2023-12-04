package GUI;

import app.Statistics;
import app.Warehouse;
import javafx.scene.chart.BarChart;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GraphController implements javafx.fxml.Initializable{
    private BarChart<?,?> barChart;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Statistics statistics = new Statistics();
        statistics.setBarChart(barChart);
    }
    public void backToStatistics() throws IOException {
        Warehouse.getInstance().changeScene("statisticsMainPage.fxml");
    }
}
