package GUI;
import app.Warehouse;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

import java.io.IOException;

public class ChooseProductToMoveController {
    @FXML
    private ChoiceBox productsOnPallet;

    private void fillProductsOnPallet(){
    }
    public void backToInitialPosition() throws IOException {
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.changeScene("moveProductFromForm.fxml");
    }
    public void confirmProductToMove() throws IOException {
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.changeScene("moveProductFinalPosition.fxml");
    }
}
