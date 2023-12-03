package GUI;

import app.Warehouse;

import java.io.IOException;

public class OrderCustomerSelectionController {

    public void backToMenu() throws IOException {
       Warehouse.getInstance().changeScene("mainMenu.fxml");
    }

    public void confirmOrderCustomer() throws IOException {
        Warehouse.getInstance().changeScene("orderProductsForm.fxml");
    }
}
