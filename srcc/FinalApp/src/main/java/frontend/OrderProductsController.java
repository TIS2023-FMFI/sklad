package Strecno;

import java.io.IOException;

public class OrderProductsController {
    public void backToCustomerSelection() throws IOException {
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.changeScene("orderCustomerSelectionForm.fxml");
    }
    public void confirmOrderProducts() throws IOException {
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.changeScene("orderShowPositionsForm.fxml");
    }
}
