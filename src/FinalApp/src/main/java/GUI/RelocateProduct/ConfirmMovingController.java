package GUI.RelocateProduct;

import Entity.Position;
import app.DatabaseHandler;
import app.RelocateProduct;
import app.Warehouse;

import java.io.IOException;
import java.util.List;

public class ConfirmMovingController {
    /***
     * Method that is called when user clicks on confirm button. It takes user back to main menu and
     * calls method that relocates product.
     * @throws IOException
     */
    public void backToMenu() throws IOException {
        MoveProductToPositionController controller = (MoveProductToPositionController)
                Warehouse.getInstance().getController("MoveProductToPositionController");

        RelocateProduct rp = new RelocateProduct();
        DatabaseHandler db = Warehouse.getInstance().getDatabaseHandler();
        //List<String> finalPositions = db.getPositionsWithPallet(controller.palletTo);
        List<String> finalPositions = controller.finalPositions;
        if (controller.isWholePallet){
            rp.relocatePallet(controller.initialPosition, controller.finalPositions, controller.palletFrom);
        } else{
            rp.relocateProduct(finalPositions, controller.initialPosition, controller.product,
                controller.quantity, controller.palletFrom, controller.palletTo);
            db.changeUserActivity(Warehouse.getInstance().currentUser.getId(), controller.palletTo);
        }

        db.changeUserActivity(Warehouse.getInstance().currentUser.getId(), controller.palletFrom);
        Warehouse.getInstance().changeScene("mainMenu.fxml");
    }
}
