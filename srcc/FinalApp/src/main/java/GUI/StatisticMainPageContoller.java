package GUI;

import app.Warehouse;

import java.io.IOException;

public class StatisticMainPageContoller {
    public void backToMenu() throws IOException {
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.changeScene("mainMenu.fxml");
    }
    public void showGraph() throws IOException {
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.changeScene("graph.fxml");
    }

    public void showInventoryCheck() throws IOException {
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.changeScene("inventoryList.fxml");
    }
    public void askForPriceForInvoicing() throws IOException {
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.changeScene("priceForInvoicing.fxml");
    }

}
