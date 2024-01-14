package GUI.RelocateProduct;
import Entity.Position;
import app.Warehouse;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class MoveProductFromPositionController {
    @FXML
    private TextField position;

    public String positionName;

    public void backToMenu() throws IOException {
        Warehouse.getInstance().changeScene("mainMenu.fxml");
    }

    public void confirmInitialPosition() throws IOException {
        if (!checkIfPositionIsCorrect(position.getText())){
            System.out.println("Position does not exist");
        }
        else {
            Warehouse.getInstance().addController("MoveProductFromPositionController", this);
            positionName = position.getText();
            Warehouse.getInstance().changeScene("RelocateProduct/chooseProductToRelocateForm.fxml");
        }
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
