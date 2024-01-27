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
    public ChoiceBox<String> newPositionsChoice;
    @FXML
    public Label errorLabel;
    @FXML
    public TextField newPNR;
    String product;
    int quantity;
    String palletFrom;
    Position initialPosition;
    boolean isWholePallet;
    List<String> finalPositions;
    //public String finalPosition;
    String palletTo;
    public int palletWeigh = 500;

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
                    palletWeigh = pallet.getWeight();
                    break;
                }
            }
        } else{
            product = controller.finalMaterial;
            quantity = controller.finalQuantity;
        }
        if (palletWeigh == 500) fillNewPositionsChoice(); //if i am relocating single material, the weight is 500
        if (palletWeigh == 1000) fillNew2PositionsChoice();
        if (palletWeigh == 1200) fillNew3PositionsChoice();
        if (palletWeigh == 2000) fillNew4PositionsChoice();
    }

    private void fillNewPositionsChoice(){
        //fills newPositionsChoice with positions that can store product
        DatabaseHandler dbh = Warehouse.getInstance().getDatabaseHandler();
        Customer customer = dbh.getCustomerThatReservedPosition(initialPosition);
        List<Position> positions = dbh.getPositionsReservedByCustomer(customer.getName());
        boolean tall = initialPosition.isTall();
        for (Position position : positions){
            if (!position.equals(initialPosition) && position.isTall() == tall)
                newPositionsChoice.getItems().add(position.getName());
        }
    }

    private void fillNew2PositionsChoice(){
        DatabaseHandler dbh = Warehouse.getInstance().getDatabaseHandler();
        Customer customer = dbh.getCustomerThatReservedPosition(initialPosition);
        List<Position> emptyPositions = dbh.getPositionsReservedByCustomer(customer.getName());
        List<String> posNames = emptyPositions.stream().map(Position::getName).toList();
        for (String positionName : posNames){
            String Wrow = positionName.substring(0, 1);
            String col = positionName.substring(1, 4);
            String row = positionName.substring(4, 5);
            int colInt = Integer.parseInt(col);
            int colInt1 = colInt + 2;
            StringBuilder zeroes = new StringBuilder();
            zeroes.append("0".repeat(Math.max(0, 3 - String.valueOf(colInt1).length())));
            String neighPos = Wrow + zeroes + colInt1 + row;
            if (posNames.contains(neighPos)){
                newPositionsChoice.getItems().add(positionName + "," + neighPos);
            }
        }
    }
    private void fillNew3PositionsChoice(){
        DatabaseHandler dbh = Warehouse.getInstance().getDatabaseHandler();
        Customer customer = dbh.getCustomerThatReservedPosition(initialPosition);
        List<Position> emptyPositions = dbh.getPositionsReservedByCustomer(customer.getName());
        List<String> posNames = emptyPositions.stream().map(Position::getName).toList();
        for (String positionName : posNames){
            String Wrow = positionName.substring(0, 1);
            String col = positionName.substring(1, 4);
            String row = positionName.substring(4, 5);
            int colInt = Integer.parseInt(col);
            int colInt1 = colInt + 2;
            int colInt2 = colInt + 4;
            StringBuilder zeroes1 = new StringBuilder();
            zeroes1.append("0".repeat(Math.max(0, 3 - String.valueOf(colInt1).length())));
            String neighPos1 = Wrow + zeroes1 + colInt1 + row;

            StringBuilder zeroes2 = new StringBuilder();
            zeroes2.append("0".repeat(Math.max(0, 3 - String.valueOf(colInt2).length())));
            String neighPos2 = Wrow + zeroes2 + colInt2 + row;

            if (posNames.contains(neighPos1) && posNames.contains(neighPos2)){
                newPositionsChoice.getItems().add(positionName + "," + neighPos1 + "," + neighPos2);
            }
        }
    }
    private void fillNew4PositionsChoice(){
        DatabaseHandler dbh = Warehouse.getInstance().getDatabaseHandler();
        Customer customer = dbh.getCustomerThatReservedPosition(initialPosition);
        List<Position> emptyPositions = dbh.getPositionsReservedByCustomer(customer.getName());
        List<String> posNames = emptyPositions.stream().map(Position::getName).toList();
        for (String positionName : posNames){
            String Wrow = positionName.substring(0, 1);
            String col = positionName.substring(1, 4);
            String row = positionName.substring(4, 5);
            int colInt = Integer.parseInt(col);
            int colInt1 = colInt + 2;
            int colInt2 = colInt + 4;
            int colInt3 = colInt + 6;
            StringBuilder zeroes1 = new StringBuilder();
            zeroes1.append("0".repeat(Math.max(0, 3 - String.valueOf(colInt1).length())));
            String neighPos1 = Wrow + zeroes1 + colInt1 + row;

            StringBuilder zeroes2 = new StringBuilder();
            zeroes2.append("0".repeat(Math.max(0, 3 - String.valueOf(colInt2).length())));
            String neighPos2 = Wrow + zeroes2 + colInt2 + row;

            StringBuilder zeroes3 = new StringBuilder();
            zeroes3.append("0".repeat(Math.max(0, 3 - String.valueOf(colInt3).length())));
            String neighPos3 = Wrow + zeroes3 + colInt3 + row;

            if (posNames.contains(neighPos1) && posNames.contains(neighPos2) && posNames.contains(neighPos3)){
                newPositionsChoice.getItems().add(positionName + "," + neighPos1 + "," + neighPos2 + "," + neighPos3);
            }
        }
    }

    public void backToProductChoice() throws IOException {
        Warehouse.getInstance().changeScene("RelocateProduct/chooseProductToRelocateForm.fxml");
    }

    public void confirmFinalPosition() throws IOException {
        if (newPositionsChoice.getValue() == null){
            errorLabel.setText("No position chosen");
            return;
        }
        finalPositions = Arrays.asList(newPositionsChoice.getValue().split(","));
        if (finalPositions.size() == 0){
            errorLabel.setText("No position chosen");
            return;
        }
        if (!isWholePallet) {
            if (!checkIfPNRCorrect(newPNR.getText())) {
                errorLabel.setText("PNR incorrect");
                return;
            }
            if (!checkIfPNRFree(newPNR.getText())) {
                errorLabel.setText("PNR already used on a different position");
                return;
            }
            palletTo = newPNR.getText();
        }
        Warehouse.getInstance().addController("MoveProductToPositionController", this);
        Warehouse.getInstance().changeScene("RelocateProduct/confirmMovingForm.fxml");
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
        Position position = Warehouse.getInstance().getDatabaseHandler().getPosition(finalPositions.get(0));
        return Warehouse.getInstance().getPalletsOnPositionMap().get(position).containsKey(pallet);
    }

    public void resetPallet(MouseEvent mouseEvent) {
        newPNR.setText("");
    }
}
