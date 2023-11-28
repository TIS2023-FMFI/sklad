package GUI;

import app.Warehouse;

import java.io.IOException;

public class OrderShowPositionsController {
    public void backToCustomerSelection() throws IOException {
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.changeScene("orderCustomerSelectionForm.fxml");
    }
    public void saveOrderAndContinue() throws IOException {
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.changeScene("orderDownloadConfirmation.fxml");
    }
}
