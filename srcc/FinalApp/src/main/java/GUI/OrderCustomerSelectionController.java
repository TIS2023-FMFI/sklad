package GUI;

import app.Warehouse;

import java.io.IOException;

public class OrderCustomerSelectionController {

    public void backToMenu() throws IOException {
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.changeScene("mainMenu.fxml");
    }

    public void confirmOrderCustomer() throws IOException {
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.changeScene("orderProductsForm.fxml");
    }
}
