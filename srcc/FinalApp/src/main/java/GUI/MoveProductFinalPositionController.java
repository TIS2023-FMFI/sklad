package GUI;

import app.Warehouse;

import java.io.IOException;

public class MoveProductFinalPositionController {
    public void backToProductChoice() throws IOException {
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.changeScene("chooseProductToMoveForm.fxml");
    }

    public void confirmFinalPosition() throws IOException {
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.changeScene("confirmMovingForm.fxml");
    }
}
