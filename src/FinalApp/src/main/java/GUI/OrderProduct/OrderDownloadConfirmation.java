package GUI.OrderProduct;

import app.FileExporter;
import app.Warehouse;

import java.io.IOException;

public class OrderDownloadConfirmation {

    public void backToMenu() throws IOException {
        OrderShowPositionsController cont = (OrderShowPositionsController)
                Warehouse.getInstance().getController("OrderShowPositionsController");


    }
}
