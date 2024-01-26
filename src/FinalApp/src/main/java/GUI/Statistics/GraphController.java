package GUI.Statistics;

import app.Statistics;
import app.Warehouse;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.List;
import java.util.ResourceBundle;

public class GraphController implements Initializable {
    public Label valueLabel;
    @FXML
    private BarChart<String,Number> barChart;

    /***
     * This method is called when the page is opened. It fills the chart with data.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        valueLabel.setVisible(false);
        StatisticMainPageController input = (StatisticMainPageController) Warehouse.getInstance().getController("statisticsMainPage");
        Date dateFrom = input.dateFromValue;
        Date dateTo = input.dateToValue;
        String customer = input.customer.getValue();

        Statistics statistics = new Statistics();

        barChart.setTitle("Počet exportovaných a importovaných paliet");

        List<XYChart.Series<String, Number>> series = statistics.setBarChart(dateFrom, dateTo, customer);
        barChart.getData().addAll(series);
        System.out.println(series);


        for (XYChart.Series<String, Number> dataSeries : barChart.getData()) {
            for (XYChart.Data<String, Number> data : dataSeries.getData()) {
                Tooltip tooltip = new Tooltip(data.getYValue() + "");
                Tooltip.install(data.getNode(), tooltip);

                data.getNode().setOnMouseEntered(event -> {
                    valueLabel.setText(data.getYValue().toString());
                    valueLabel.setVisible(true);
                });

                data.getNode().setOnMouseExited(event -> {
                    valueLabel.setVisible(false);
                });
            }
        }
        //barChart.setStyle("-fx-bar-gap: 10; -fx-bar-width: 60;");
    }

    public void backToStatistics() throws IOException {
        Warehouse.getInstance().changeScene("Statistics/statisticsMainPageForm.fxml");
    }
}
