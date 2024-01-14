package GUI.OrderProduct;

import app.Warehouse;

import java.io.IOException;

public class OrderDownloadConfirmation {

    public void backToMenu() throws IOException {
        Warehouse.getInstance().changeScene("mainMenu.fxml");
    }
}
