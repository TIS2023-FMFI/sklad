package GUI;

import app.Warehouse;

import java.io.IOException;

public class MoveProductFinalPositionController {
    public void backToProductChoice() throws IOException {
        Warehouse.getInstance().changeScene("chooseProductToMoveForm.fxml");
    }

    public void confirmFinalPosition() throws IOException {
        Warehouse.getInstance().changeScene("confirmMovingForm.fxml");
    }
}
