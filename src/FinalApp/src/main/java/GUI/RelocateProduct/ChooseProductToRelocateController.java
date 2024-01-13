package GUI.RelocateProduct;
import app.Warehouse;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

import java.io.IOException;

public class ChooseProductToRelocateController {
    @FXML
    private ChoiceBox productsOnPallet;

    private void fillProductsOnPallet(){
    }
    public void backToInitialPosition() throws IOException {
        Warehouse.getInstance().changeScene("RelocateProduct/moveProductFromPositionForm.fxml");
    }
    public void confirmProductToMove() throws IOException {
        Warehouse.getInstance().changeScene("RelocateProduct/moveProductToPositionForm.fxml");
    }
}
