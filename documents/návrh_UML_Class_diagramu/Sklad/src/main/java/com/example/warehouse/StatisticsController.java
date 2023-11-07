package com.example.warehouse;

import java.io.IOException;

public class StatisticsController {

    public void backToMenu() throws IOException {
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.changeScene("mainMenu.fxml");
    }

    public void backToStatistics() throws IOException {
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.changeScene("statisticsMainPage.fxml");
    }

    public void showGraph() throws IOException {
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.changeScene("graph.fxml");
    }

    public void showInventoryCheck() throws IOException {
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.changeScene("inventoryCheck.fxml");
    }

    public void askForPriceForInvoicing() throws IOException {
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.changeScene("addPriceForInvoicing.fxml");
    }

    public void calculateInvoicing() throws IOException {
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.changeScene("calculatedInvoicing.fxml");
    }
}
