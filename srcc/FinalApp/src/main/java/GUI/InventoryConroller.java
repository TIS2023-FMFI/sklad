package GUI;

import app.Warehouse;

import java.io.IOException;

public class InventoryConroller {
    public void printInventoryList(){
    }

    public void backToStatistics() throws IOException {
        Warehouse.getInstance().changeScene("statisticsMainPage.fxml");
    }
}
