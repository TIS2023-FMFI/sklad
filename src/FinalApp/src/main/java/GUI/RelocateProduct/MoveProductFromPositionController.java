package GUI.RelocateProduct;
import Entity.Position;
import app.Warehouse;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class MoveProductFromPositionController {
    @FXML
    private CheckBox wholePallet;
    @FXML
    private Label errorLabel;

    /***
     * Flag that is set to true if user wants to relocate whole pallet.
     */
    boolean isWholePallet = false;
    @FXML
    private TextField position;

    /***
     * Variable that stores name of position that user entered.
     */
    public String positionName;

    /***
     * Method that is called when user clicks on back button. It takes user back to main menu.
     * @throws IOException if there is problem with loading fxml file
     */
    public void backToMenu() throws IOException {
        Warehouse.getInstance().changeScene("mainMenu.fxml");
    }

    /***
     * Method that is called when user clicks on confirm button. It checks if position exists and if it does,
     * it takes user to next scene.
     * @throws IOException if there is problem with loading fxml file
     */
    public void confirmInitialPosition() throws IOException {
        if (!checkIfPositionIsCorrect(position.getText())){
            errorLabel.setText("Pozícia neexistuje");
            return;
        }
        if (Warehouse.getInstance().getDatabaseHandler().getPalletesOnPosition(position.getText()).isEmpty()){
            errorLabel.setText("Na pozícii sa nenachádza žiadny produkt");
            return;
        }

        isWholePallet = wholePallet.isSelected();
        Warehouse.getInstance().addController("MoveProductFromPositionController", this);
        positionName = position.getText();
        Warehouse.getInstance().changeScene("RelocateProduct/chooseProductToRelocateForm.fxml");
    }

    private boolean checkIfPositionIsCorrect(String position){
        //checks memory if position exists
        var positions = Warehouse.getInstance().getPositionsInRows();
        for (var regals : positions.values()){
            for (List<Position> positionList : regals.values()){
                for (Position position1 : positionList){
                    if (position1.getName().equals(position)){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
