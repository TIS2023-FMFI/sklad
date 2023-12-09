package GUI.OrderProduct;

import app.Warehouse;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class OrderCustomerSelectionController {

    public void backToMenu() throws IOException {
       Warehouse.getInstance().changeScene("mainMenu.fxml");
    }

    public void confirmOrderCustomer() throws IOException {
        Warehouse.getInstance().changeScene("OrderProduct/orderProductsForm.fxml");
    }
}
