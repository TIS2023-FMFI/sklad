package GUI;

import app.Warehouse;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.io.IOException;

public class StoreInPositionController {
    @FXML
    private ChoiceBox<String> position;

    @FXML
    private TextField note;

    // algoritmus nájde najvhodnejšiu pozíciu na uskladnenie
    public Position findFreePosition(){
        return null;
    }

    // nájde všetky voľné pozície vhodné na uskladnenie palety
    public Position[] findAllFreePositions(){
        return null;
    }

    // setne pozíciu, poznámku, dátum
    // zaskladní paletu (pridá záznam do databázy)
    // presunie na main menu
    public void storeInProduct() throws IOException {
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.changeScene("mainMenu.fxml");
    }

    // vykoná storeInProduct a pokračuje v zaskladňovaní
    // vynuluje doteraz zadané informácie okrem informácií z prvého formuláru (customerTockaForm)
    // nezabudnúť na vynulovanie taktiež materialCount
    public void continueStoringIn() throws IOException{
        storeInProduct();
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.changeScene("palletInformationForm.fxml");
    }
}
