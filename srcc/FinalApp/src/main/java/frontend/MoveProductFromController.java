package Strecno;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;

public class MoveProductFromController {
    @FXML
    private TextField position;

    public void backToMenu() throws IOException {
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.changeScene("mainMenu.fxml");
    }
    public void confirmInitialPosition() throws IOException {
        if (!checkIfPositionIsCorrect(position.getText())){

        }
        else {
            System.out.println(position.getText());
            Warehouse warehouse = Warehouse.getInstance();
            warehouse.changeScene("chooseProductToMoveForm.fxml");
            //fillProductList();
        }
    }

    private boolean checkIfPositionIsCorrect(String position){
        return true;
    }
}
