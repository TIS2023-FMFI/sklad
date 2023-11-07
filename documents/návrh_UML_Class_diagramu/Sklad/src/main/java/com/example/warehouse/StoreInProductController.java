package com.example.warehouse;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.io.IOException;

public class StoreInProductController {

    private Pallet pallet;
    private int materialCount;
    @FXML
    private TextField customer;
    @FXML
    private TextField tockaNumber;
    @FXML
    private TextField PNR;
    @FXML
    private CheckBox oversizedInHeight;
    @FXML
    private CheckBox damaged;
    @FXML
    private ChoiceBox<String> palletType;
    @FXML
    private ChoiceBox<String> position;
    @FXML
    private TextField note;
    private final String[] palletTypeOptions = {"Europaleta", "Americká paleta", "GitterBox"};

    public StoreInProductController() {
        pallet = new Pallet();
        materialCount = 0;
    }

    public void backToMenu() throws IOException {
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.changeScene("mainMenu.fxml");
    }

    // setne informácie zákazník, číslo točky, použivateľa pre paletu
    // presunutie na ďalší formulár kde zákazník vyplní informácie o produkte
    public void nextToInformationForm() throws IOException {
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.changeScene("informationForm.fxml");
    }

    public void backToCustomerTockaForm() throws IOException {
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.changeScene("customerTockaForm.fxml");
    }

    // algoritmus nájde najvhodnejšiu pozíciu na uskladnenie
    public Position findFreePosition(){
        return null;
    }

    // nájde všetky voľné pozície vhodné na uskladnenie palety
    public Position[] findAllFreePositions(){
        return null;
    }

    // setne informácie PNR, nadrozmernosť, poškodenosť, hmotnosť, typ palety, materiály a ich množstvo
    public void nextToPositionForm() throws IOException {
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.changeScene("positionForm.fxml");

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

    // setne pozíciu, poznámku, dátum
    // zaskladní paletu (pridá záznam do databázy)
    // presunie na main menu
    public void storeInProduct() throws IOException{
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.changeScene("mainMenu.fxml");
    }

    // vykoná storeInProduct a pokračuje v zaskladňovaní
    // vynuluje doteraz zadané informácie okrem informácií z prvého formuláru (customerTockaForm)
    // nezabudnúť na vynulovanie taktiež materialCount
    public void continueStoringIn() throws IOException{
        storeInProduct();
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.changeScene("informationForm.fxml");
    }
}
