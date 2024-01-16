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
        DatabaseHandler databaseHandler = Warehouse.getInstance().getDatabaseHandler();
        Position initialPosition = controller.initialPosition;
        Position finalPosition = databaseHandler.getPosition(controller.finalPosition);

        RelocateProduct rp = new RelocateProduct();
        rp.relocateProduct(finalPosition, initialPosition, controller.product,
                controller.quantity, controller.palletFrom, controller.palletTo);



        Warehouse.getInstance().changeScene("mainMenu.fxml");
    }
}
