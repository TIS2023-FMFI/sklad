package GUI;
import app.Warehouse;

import java.io.IOException;

public class MainMenuController {
    public void showWarehouseLayout() throws IOException {
        Warehouse.getInstance().changeScene("warehouseLayoutRows.fxml");
    }

    public void storeInProduct() throws IOException {
        Warehouse.getInstance().changeScene("customerTruckNumberForm.fxml");
    }

    public void relocateProduct() throws IOException {
        Warehouse.getInstance().changeScene("moveProductFromForm.fxml");
    }

    public void placeOrder() throws IOException {
        Warehouse.getInstance().changeScene("orderCustomerSelectionForm.fxml");
    }

    public void showStatistics() throws IOException {
        Warehouse.getInstance().changeScene("statisticsMainPage.fxml");
    }

    public void logout() throws IOException {
         Warehouse.getInstance().changeScene("login.fxml");
    }
}
