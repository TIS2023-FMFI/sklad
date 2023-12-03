package GUI;

import app.Warehouse;

import java.io.IOException;

public class GraphController {
    public void backToStatistics() throws IOException {
        Warehouse.getInstance().changeScene("statisticsMainPage.fxml");
    }
}
