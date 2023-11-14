package Strecno;

import java.io.IOException;

public class PriceForInvoicingController {
    public void backToStatistics() throws IOException {
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.changeScene("statisticsMainPage.fxml");
    }
    public void calculateInvoicing() throws IOException {
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.changeScene("calculatedInvoicing.fxml");
    }
}
