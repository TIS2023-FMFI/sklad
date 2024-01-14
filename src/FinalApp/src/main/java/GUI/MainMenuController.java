package GUI;
import app.Warehouse;

import java.io.IOException;

public class MainMenuController {
    public void showWarehouseLayout() throws IOException {
        Warehouse.getInstance().changeScene("WarehouseLayout/warehouseLayoutRowsForm.fxml");
    }

    public void storeInProduct() throws IOException {
        Warehouse.getInstance().initializeStoreInProduct();
        Warehouse.getInstance().changeScene("StoreInProduct/customerTruckNumberForm.fxml");
    }

    public void relocateProduct() throws IOException {
        Warehouse.getInstance().changeScene("RelocateProduct/moveProductFromPositionForm.fxml");
    }

    public void placeOrder() throws IOException {
        Warehouse.getInstance().changeScene("OrderProduct/orderCustomerSelectionForm.fxml");
    }

    public void showStatistics() throws IOException {
        Warehouse.getInstance().changeScene("Statistics/statisticsMainPageForm.fxml");
    }

    public void reservationsMain() throws IOException {
        Warehouse.getInstance().changeScene("Reservations/reservationsMain.fxml");
    }

    public void logout() throws IOException {
         Warehouse.getInstance().changeScene("login.fxml");
    }
}
