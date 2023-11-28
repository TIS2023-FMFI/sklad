package GUI;

import app.Warehouse;

import java.io.IOException;

public class WarehouseLayoutController {
    public void backToMenu() throws IOException {
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.changeScene("mainMenu.fxml");
    }
}
