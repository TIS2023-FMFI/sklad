package GUI.WarehouseLayout;

import app.Warehouse;

import java.io.IOException;

public class WarehouseLayoutController {
    public void backToMenu() throws IOException {
        Warehouse.getInstance().changeScene("mainMenu.fxml");
    }
}
