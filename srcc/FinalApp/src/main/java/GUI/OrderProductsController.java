package GUI;

import app.Warehouse;

import java.io.IOException;

public class OrderProductsController {
    public void backToCustomerSelection() throws IOException {
        Warehouse.getInstance().changeScene("orderCustomerSelectionForm.fxml");
    }
    public void confirmOrderProducts() throws IOException {
        Warehouse.getInstance().changeScene("orderShowPositionsForm.fxml");
    }
}
