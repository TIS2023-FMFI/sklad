package GUI.RelocateProduct;

import app.Warehouse;

import java.io.IOException;

public class ConfirmMovingController {
    public void backToMenu() throws IOException {
        Warehouse.getInstance().changeScene("mainMenu.fxml");
    }
}
