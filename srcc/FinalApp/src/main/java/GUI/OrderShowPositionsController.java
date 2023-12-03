package GUI;

import app.Warehouse;

import java.io.IOException;

public class OrderShowPositionsController {
    public void backToCustomerSelection() throws IOException {
        Warehouse.getInstance().changeScene("orderCustomerSelectionForm.fxml");
    }
    public void saveOrderAndContinue() throws IOException {
        Warehouse.getInstance().changeScene("orderDownloadConfirmation.fxml");
    }
}
