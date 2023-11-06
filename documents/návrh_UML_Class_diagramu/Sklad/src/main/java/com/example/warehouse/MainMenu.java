package com.example.warehouse;
import java.io.IOException;

public class MainMenu {
    // neviem ešte ako/kde ale sú potrebné
    // private User user --> user, ktorý je práve prihlásený
    // private Map<String, Map<String, Position[]>> warehouseLayout --> a jeho načítanie z databázy

    public void showWarehouseLayout() throws IOException {
        Warehouse warehouse = new Warehouse();
        warehouse.changeScene("warehouseLayoutRows.fxml");
    }

    public void storeInProduct() throws IOException {
        Warehouse warehouse = new Warehouse();
        warehouse.changeScene("customerTockaForm.fxml");
    }

    public void relocateProduct() throws IOException {
        Warehouse warehouse = new Warehouse();
        warehouse.changeScene("moveFromForm.fxml");
    }

    public void placeOrder() throws IOException {
        Warehouse warehouse = new Warehouse();
        warehouse.changeScene("orderCustomerSelectionForm.fxml");
    }

    public void showStatistics() throws IOException {
        Warehouse warehouse = new Warehouse();
        warehouse.changeScene("statisticsMainPage.fxml");
    }

    public void logout() throws IOException {
        Warehouse warehouse = new Warehouse();
        warehouse.changeScene("login.fxml");
    }
}
