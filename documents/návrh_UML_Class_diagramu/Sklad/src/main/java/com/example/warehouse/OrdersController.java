package com.example.warehouse;

import java.io.IOException;

public class OrdersController {

    public void backToMenu() throws IOException {
        Warehouse warehouse = new Warehouse();
        warehouse.changeScene("mainMenu.fxml");
    }

    public void backToCustomerSelection() throws IOException {
        Warehouse warehouse = new Warehouse();
        warehouse.changeScene("orderCustomerSelectionForm.fxml");
    }

    public void confirmOrderCustomer() throws IOException {
        Warehouse warehouse = new Warehouse();
        warehouse.changeScene("orderAddProductsForm.fxml");
    }

    public void confirmOrderProducts() throws IOException {
        Warehouse warehouse = new Warehouse();
        warehouse.changeScene("orderShowPositionsForm.fxml");
    }

    public void saveOrderAndContinue() throws IOException {
        Warehouse warehouse = new Warehouse();
        warehouse.changeScene("orderDownloadConfirmation.fxml");
    }
}
