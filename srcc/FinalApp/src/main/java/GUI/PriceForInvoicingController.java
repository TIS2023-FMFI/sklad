package GUI;

import app.Warehouse;

import java.io.IOException;

public class PriceForInvoicingController {
    public void backToStatistics() throws IOException {
        Warehouse.getInstance().changeScene("statisticsMainPage.fxml");
    }
    public void calculateInvoicing() throws IOException {
        Warehouse.getInstance().changeScene("calculatedInvoicing.fxml");
    }
}
