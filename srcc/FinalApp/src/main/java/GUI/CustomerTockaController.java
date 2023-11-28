package GUI;

import app.Warehouse;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;

public class CustomerTockaController {
    @FXML
    private TextField customer;
    @FXML
    private TextField tockaNumber;

    public void backToMenu() throws IOException {
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.changeScene("mainMenu.fxml");
    }

    // setne informácie zákazník, číslo točky, použivateľa pre paletu
    // presunutie na ďalší formulár kde zákazník vyplní informácie o produkte
    public void nextToInformationForm() throws IOException {
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.changeScene("palletInformationForm.fxml");
    }
}
