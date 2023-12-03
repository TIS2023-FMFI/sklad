package GUI;

import app.Warehouse;

import java.io.IOException;

public class StatisticMainPageContoller {
    public void backToMenu() throws IOException {
        Warehouse.getInstance().changeScene("mainMenu.fxml");
    }
    public void showGraph() throws IOException {
        Warehouse.getInstance().changeScene("graph.fxml");
    }

    public void showInventoryCheck() throws IOException {
        Warehouse.getInstance().changeScene("inventoryList.fxml");
    }
    public void askForPriceForInvoicing() throws IOException {
        Warehouse.getInstance().changeScene("priceForInvoicing.fxml");
    }

}
