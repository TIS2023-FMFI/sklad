package Strecno;

import java.io.IOException;

public class CalculatedInvoicingController {
    public void backToStatistics() throws IOException {
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.changeScene("statisticsMainPage.fxml");
    }
    public void backToMenu() throws IOException {
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.changeScene("mainMenu.fxml");
    }
    public void saveInvoicingPrice(){

    }
}
