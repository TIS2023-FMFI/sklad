package GUI.RelocateProduct;

import app.Warehouse;

import java.io.IOException;

public class MoveProductToPositionController {
    public void backToProductChoice() throws IOException {
        Warehouse.getInstance().changeScene("RelocateProduct/chooseProductToRelocateForm.fxml");
    }

    public void confirmFinalPosition() throws IOException {
        Warehouse.getInstance().changeScene("RelocateProduct/confirmMovingForm.fxml");
    }
}
