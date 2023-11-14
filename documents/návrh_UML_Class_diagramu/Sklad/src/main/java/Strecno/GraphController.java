package Strecno;

import java.io.IOException;

public class GraphController {
    public void backToStatistics() throws IOException {
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.changeScene("statisticsMainPage.fxml");
    }
}
