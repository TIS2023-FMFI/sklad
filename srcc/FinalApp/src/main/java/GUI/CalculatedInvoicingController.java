package GUI;
import app.Warehouse;

import java.io.IOException;

public class CalculatedInvoicingController {
    public void backToStatistics() throws IOException {
        Warehouse.getInstance().changeScene("statisticsMainPage.fxml");
    }
    public void backToMenu() throws IOException {
        Warehouse.getInstance().changeScene("mainMenu.fxml");
    }
    public void saveInvoicingPrice(){

    }
}
