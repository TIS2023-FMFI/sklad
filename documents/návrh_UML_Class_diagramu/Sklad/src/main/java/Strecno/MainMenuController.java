package Strecno;
import java.io.IOException;

public class MainMenuController {
    // neviem ešte ako/kde ale sú potrebné
    // private User user --> user, ktorý je práve prihlásený
    // private Map<String, Map<String, Position[]>> warehouseLayout --> a jeho načítanie z databázy

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
