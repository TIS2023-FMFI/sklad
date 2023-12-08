package GUI.RelocateProduct;
import app.Warehouse;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;

public class MoveProductFromPositionController {
    @FXML
    private TextField position;

    public void backToMenu() throws IOException {
        Warehouse.getInstance().changeScene("mainMenu.fxml");
    }
    public void confirmInitialPosition() throws IOException {
        if (!checkIfPositionIsCorrect(position.getText())){

        }
        else {
            System.out.println(position.getText());
            Warehouse.getInstance().changeScene("RelocateProduct/chooseProductToRelocateForm.fxml");
            //fillProductList();
        }
    }

    private boolean checkIfPositionIsCorrect(String position){
        return true;
    }
}
