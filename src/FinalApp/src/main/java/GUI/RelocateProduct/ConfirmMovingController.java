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
        DatabaseHandler db = Warehouse.getInstance().getDatabaseHandler();
        if (controller.isWholePallet){
            rp.relocatePallet(controller.initialPosition, controller.finalPositions, controller.palletFrom);
        } else{
            //staci prvy prvok z finalPositions lebo ked premiestnujem jeden material, tak sa to premiestni na jednu poziciu
            rp.relocateProduct(controller.finalPositions.get(0), controller.initialPosition, controller.product,
                controller.quantity, controller.palletFrom, controller.palletTo);
            db.changeUserActivity(Warehouse.getInstance().currentUser.getId(), controller.palletTo);
        }

        db.changeUserActivity(Warehouse.getInstance().currentUser.getId(), controller.palletFrom);
        Warehouse.getInstance().changeScene("mainMenu.fxml");
    }
}
