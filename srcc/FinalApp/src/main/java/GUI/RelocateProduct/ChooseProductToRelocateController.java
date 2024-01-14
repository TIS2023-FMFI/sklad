package GUI.RelocateProduct;
import app.Warehouse;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChooseProductToRelocateController implements Initializable {
    @FXML
    private ChoiceBox productsOnPallet;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MoveProductFromPositionController controller = (MoveProductFromPositionController)
                Warehouse.getInstance().getController("MoveProductFromPositionController");

    }

    private void fillProductsOnPallet(String position){
        //fills productsOnPallet with products on position:
    }

    public void backToInitialPosition() throws IOException {
        Warehouse.getInstance().changeScene("RelocateProduct/moveProductFromPositionForm.fxml");
    }
    public void confirmProductToMove() throws IOException {
        Warehouse.getInstance().changeScene("RelocateProduct/moveProductToPositionForm.fxml");
    }
}
