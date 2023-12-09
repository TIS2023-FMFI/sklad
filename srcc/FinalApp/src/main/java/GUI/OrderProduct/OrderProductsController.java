package GUI.OrderProduct;

import app.Warehouse;

import java.io.IOException;

public class OrderProductsController {
    public void backToCustomerSelection() throws IOException {
        Warehouse.getInstance().changeScene("OrderProduct/orderCustomerSelectionForm.fxml");
    }
    public void confirmOrderProducts() throws IOException {
        Warehouse.getInstance().changeScene("OrderProduct/orderShowPositionsForm.fxml");
    }
}
