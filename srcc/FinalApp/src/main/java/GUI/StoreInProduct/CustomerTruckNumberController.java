package GUI.StoreInProduct;

import app.Warehouse;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;

public class CustomerTruckNumberController {
    @FXML
    private TextField customer;
    @FXML
    private TextField truckNumber;

    public void backToMenu() throws IOException {
        Warehouse.getInstance().changeScene("mainMenu.fxml");
    }

    // setne informácie zákazník, číslo točky, použivateľa pre paletu
    // presunutie na ďalší formulár kde zákazník vyplní informácie o produkte
    public void nextToInformationForm() throws IOException {
        Warehouse.getInstance().changeScene("StoreInProduct/palletInformationForm.fxml");
    }
}
