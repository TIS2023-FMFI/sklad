package GUI.RelocateProduct;

import Entity.Customer;
import Entity.Pallet;
import Entity.Position;
import app.DatabaseHandler;
import app.Warehouse;
import jakarta.persistence.criteria.CriteriaBuilder;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class MoveProductToPositionController implements Initializable {
    @FXML
    private ChoiceBox<String> newPositionsChoice;
    @FXML
    private Label errorLabel;
    @FXML
    private TextField newPNR;
    /***
     * Variable that stores name of product that user chose to move.
     */
    String product;
    /***
     * Variable that stores quantity of product that user chose to move.
     */
    int quantity;
    /***
     * Variable that stores name of pallet which user is moving the material from.
     */
    String palletFrom;
    /***
     * Variable that stores initial position of pallet which user is moving.
     */
    Position initialPosition;
    /***
     * Variable that stores if user wants to move whole pallet or just some material.
     */
    boolean isWholePallet;
    /***
     * Variable that stores final positions of pallet which user is moving.
     */
    List<String> finalPositions;
    /***
     * Variable that stores final PNR of pallet which user is moving material to.
     */
    String palletTo;
    /***
     * Variable that stores width of moved pallet.
     */
    public int palletWidth = 1;
    private double weight = 0;
    /***
     * Method that fills newPositionsChoice with correct positions.
     * @throws IOException if there is problem with loading fxml file
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ChooseProductToRelocateController controller = (ChooseProductToRelocateController)
                Warehouse.getInstance().getController("ChooseProductToRelocateController");
        palletFrom = controller.finalPallet;
        initialPosition = controller.initialPosition;
        isWholePallet = controller.isWholePallet;
        if (isWholePallet){
            newPNR.setDisable(true);
            var palsOnPos = Warehouse.getInstance().getPalletsOnPositionMap().get(initialPosition);
            for (var pallet : palsOnPos.keySet()){
                if (pallet.getPnr().equals(palletFrom)){
                    palletWidth = pallet.getNumberOfPositions();
                    weight = pallet.getWeight();;
                    break;
                }
            }
        } else{
            product = controller.finalMaterial;
            quantity = controller.finalQuantity;
        }
        fillNewPositionsChoice();
    }

    private void fillNewPositionsChoice(){
        DatabaseHandler dbh = Warehouse.getInstance().getDatabaseHandler();
        Customer customer = dbh.getCustomerThatReservedPosition(initialPosition);
        List<List<Position>> freePositions = dbh.getFreePositions(customer, weight, false, palletWidth);
        List<Position> currentPositions = dbh.getPositionsWithPallet(palletFrom).stream().map(dbh::getPosition).toList();
        freePositions.remove(currentPositions);
        if (freePositions.size() == 0){
            errorLabel.setText("Nenašli sa voľné pozície.");
            return;
        }

        newPositionsChoice.getItems().addAll(getToString(freePositions));
    }

    private List<String> getToString(List<List<Position>> freePositions) {
        return freePositions.stream().map(positions -> {
            var posNames = positions.stream().map(Position::getName).toList();
            return String.join("-", posNames);
        }).toList();
    }

    /***
     * Method that takes user back one step to material/pallet selection
     * @throws IOException
     */
    public void backToProductChoice() throws IOException {
        Warehouse.getInstance().changeScene("RelocateProduct/chooseProductToRelocateForm.fxml");
    }

    /***
     * Method that checks chosen positions and if they are correct, calls method to confirm moving
     * @throws IOException
     */
    public void confirmFinalPosition() throws IOException {
        if (newPositionsChoice.getValue() == null){
            errorLabel.setText("Musíte vybrať pozíciu.");
            return;
        }
        finalPositions = Arrays.asList(newPositionsChoice.getValue().split("-"));
        if (finalPositions.size() == 0){
            errorLabel.setText("Musíte vybrať pozíciu.");
            return;
        }
        if (!isWholePallet) {
            if (!checkIfPNRCorrect(newPNR.getText())) {
                errorLabel.setText("PNR nie je správne zadané.");
                return;
            }
            if (!checkIfPNRFree(newPNR.getText())) {
                errorLabel.setText("PNR sa už v sklade nachádza na inej pozícii");
                return;
            }
            palletTo = newPNR.getText();
        }
        Warehouse.getInstance().addController("MoveProductToPositionController", this);
        Warehouse.getInstance().changeScene("RelocateProduct/confirmMovingForm.fxml");
    }

    /***
     * Method that checks if a position is chosen
     */
    public void checkIfPositionFilled() {
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
        Position position = Warehouse.getInstance().getDatabaseHandler().getPosition(finalPositions.get(0));
        return Warehouse.getInstance().getPalletsOnPositionMap().get(position).containsKey(pallet);
    }

    /***
     * Method that resets the pallet number field to empty string
     */
    public void resetPallet() {
        newPNR.setText("");
    }
}
