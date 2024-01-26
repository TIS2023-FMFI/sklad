package GUI.RelocateProduct;

import Entity.Position;
import app.DatabaseHandler;
import app.RelocateProduct;
import app.Warehouse;

import java.io.IOException;

public class ConfirmMovingController {
    public void backToMenu() throws IOException {
        MoveProductToPositionController controller = (MoveProductToPositionController)
                Warehouse.getInstance().getController("MoveProductToPositionController");

        RelocateProduct rp = new RelocateProduct();
        if (controller.isWholePallet){
            rp.relocatePallet(controller.initialPosition, controller.finalPositions, controller.palletFrom);
        } else{
            //staci prvy prvok z finalPositions lebo ked premiestnujem jeden material, tak sa to premiestni na jednu poziciu
            rp.relocateProduct(controller.finalPositions.get(0), controller.initialPosition, controller.product,
                controller.quantity, controller.palletFrom, controller.palletTo);
        }


        Warehouse.getInstance().changeScene("mainMenu.fxml");
    }
}
