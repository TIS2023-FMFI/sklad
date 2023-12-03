package GUI;

import app.Warehouse;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.io.IOException;

public class PalletInformationController {
    @FXML
    private TextField PNR;
    @FXML
    private CheckBox oversizedInHeight;
    @FXML
    private CheckBox damaged;
    @FXML
    private ChoiceBox<String> palletType;
    private final String[] palletTypeOptions = {"Europaleta", "Americká paleta", "GitterBox"};

    public void backToCustomerTockaForm() throws IOException {
        Warehouse.getInstance().changeScene("customerTruckNumberForm.fxml");
    }

    // setne informácie PNR, nadrozmernosť, poškodenosť, hmotnosť, typ palety, materiály a ich množstvo
    public void nextToPositionForm() throws IOException {
        Warehouse.getInstance().changeScene("storeInPositionForm.fxml");

        // tu bude nejaký cyklus, ktorý pridá po jednom jednotlivé materiály a ich počet
        // predstavujem si to zatiaľ tak nejak, že každý Textfield nazveme, že material[jeho poradové číslo] a
        // k nemu prislúchajúci Textfield materialCount[jeho poradové číslo] aby sme s tým vedeli pracovať

        // následne to potom cyklom prejdeme podľa počtu, koľko tých materiálov bude premenná materialCount
        // a pridáme jednotlivé záznamy na paletu
    }

    // zvýši counter meterialCount a pridá do GUI políčka na záznam ďalšieho materiálu
    public void addMaterial(){

    }

    // zníži counter meterialCount a odoberie z GUI daný záznam
    // prvý záznam bude fixný, nebude možné ho odobrať (matrial0, materialCount0)
    public void removeMaterial(){

    }
}
