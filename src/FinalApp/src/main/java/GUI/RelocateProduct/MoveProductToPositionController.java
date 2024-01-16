package GUI.RelocateProduct;

import Entity.Customer;
import Entity.Pallet;
import Entity.Position;
import app.DatabaseHandler;
import app.Warehouse;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MoveProductToPositionController implements Initializable {
    public ChoiceBox<String> newPositionsChoice;
    public Label errorLabel;
    public TextField newPNR;
    String product;
    int quantity;
    String palletFrom;
    Position initialPosition;
    String finalPosition;
    String palletTo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ChooseProductToRelocateController controller = (ChooseProductToRelocateController)
                Warehouse.getInstance().getController("ChooseProductToRelocateController");
        product = controller.finalMaterial;
        quantity = controller.finalQuantity;
        palletFrom = controller.finalPallet;
        initialPosition = controller.initialPosition;

        fillNewPositionsChoice();

    }

    private void fillNewPositionsChoice(){
        //fills newPositionsChoice with positions that can store product
        DatabaseHandler dbh = Warehouse.getInstance().getDatabaseHandler();
        Customer customer = dbh.getCustomerThatReservedPosition(initialPosition);
        List<Position> positions = dbh.getPositionsReservedByCustomer(customer.getName());
        for (Position position : positions){
            if (!position.equals(initialPosition))
                newPositionsChoice.getItems().add(position.getName());
        }
    }

    public void backToProductChoice() throws IOException {
        Warehouse.getInstance().changeScene("RelocateProduct/chooseProductToRelocateForm.fxml");
    }

    public void confirmFinalPosition() throws IOException {
        finalPosition = newPositionsChoice.getValue();
        if (finalPosition == null){
            errorLabel.setText("No position chosen");
        } else if (!checkIfPNRCorrect(newPNR.getText())){
            errorLabel.setText("PNR incorrect");
        } else if (!checkIfPNRFree(newPNR.getText())) {
            errorLabel.setText("PNR already used on a different position");
        } else {
            palletTo = newPNR.getText();
            Warehouse.getInstance().addController("MoveProductToPositionController", this);
            Warehouse.getInstance().changeScene("RelocateProduct/confirmMovingForm.fxml");
        }
    }

    public void checkIfPositionFilled(KeyEvent keyEvent) {
        if (newPositionsChoice.getValue() == null){
            newPNR.setText("");
            errorLabel.setText("No position chosen");
        }
    }

    private boolean checkIfPNRCorrect(String PNR){
        //checks if PNR is correct
        int pnrNum;
        try {
            pnrNum = Integer.parseInt(PNR);
        } catch (NumberFormatException e){
            return false;
        }
        return (pnrNum > 2000 && pnrNum < 4000);
    }

    private boolean checkIfPNRFree(String PNR){
        if (!Warehouse.getInstance().getDatabaseHandler().PNRisUsed(PNR)){
            return true;
        }
        Pallet pallet = Warehouse.getInstance().getDatabaseHandler().getPallet(PNR);
        Position position = Warehouse.getInstance().getDatabaseHandler().getPosition(finalPosition);
        return Warehouse.getInstance().getPalletsOnPosition().get(position).containsKey(pallet);
    }

    public void resetPallet(MouseEvent mouseEvent) {
        newPNR.setText("");
    }
}
