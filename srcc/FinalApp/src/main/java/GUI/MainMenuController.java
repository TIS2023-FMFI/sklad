package GUI;
import app.Warehouse;

import java.io.IOException;

public class MainMenuController {
    public void showWarehouseLayout() throws IOException {
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.changeScene("warehouseLayoutRows.fxml");
    }

    public void storeInProduct() throws IOException {
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.changeScene("customerTockaForm.fxml");
    }

    public void relocateProduct() throws IOException {
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.changeScene("moveProductFromForm.fxml");
    }

    public void placeOrder() throws IOException {
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.changeScene("orderCustomerSelectionForm.fxml");
    }

    public void showStatistics() throws IOException {
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.changeScene("statisticsMainPage.fxml");
    }

    public void logout() throws IOException {
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.changeScene("login.fxml");
    }
}
