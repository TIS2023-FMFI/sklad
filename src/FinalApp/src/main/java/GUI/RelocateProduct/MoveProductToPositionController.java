package GUI.RelocateProduct;

import app.Warehouse;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MoveProductToPositionController implements Initializable {
    public ChoiceBox<String> newPositionsChoice;

    String product;
    int quantity;
    String pallet;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ChooseProductToRelocateController controller = (ChooseProductToRelocateController)
                Warehouse.getInstance().getController("ChooseProductToRelocateController");
        product = controller.finalMaterial;
        quantity = controller.finalQuantity;
        pallet = controller.finalPallet;

    }

    public void backToProductChoice() throws IOException {
        Warehouse.getInstance().changeScene("RelocateProduct/chooseProductToRelocateForm.fxml");
    }

    public void confirmFinalPosition() throws IOException {
        Warehouse.getInstance().changeScene("RelocateProduct/confirmMovingForm.fxml");
    }

}
