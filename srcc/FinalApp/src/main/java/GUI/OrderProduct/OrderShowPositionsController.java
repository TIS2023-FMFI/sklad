package GUI.OrderProduct;

import app.Warehouse;

import java.io.IOException;

public class OrderShowPositionsController {
    public void backToCustomerSelection() throws IOException {
        Warehouse.getInstance().changeScene("OrderProduct/orderCustomerSelectionForm.fxml");
    }
    public void saveOrderAndContinue() throws IOException {
        Warehouse.getInstance().changeScene("OrderProduct/orderDownloadConfirmation.fxml");
    }
}
