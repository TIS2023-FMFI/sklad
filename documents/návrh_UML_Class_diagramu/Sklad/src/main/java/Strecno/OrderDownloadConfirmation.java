package Strecno;

import java.io.IOException;

public class OrderDownloadConfirmation {

    public void backToMenu() throws IOException {
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.changeScene("mainMenu.fxml");
    }
}
