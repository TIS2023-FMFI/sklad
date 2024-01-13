package GUI.StoreInProduct;

import app.Warehouse;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class PalletInformationController implements Initializable {
    @FXML
    private BorderPane borderPane;
    @FXML
    private AnchorPane topContainer;
    @FXML
    private VBox materialContainer;
    @FXML
    private AnchorPane informationContainer;
    @FXML
    private Button addMaterialButton;
    @FXML
    private Label errorMessage;
    @FXML
    private TextField PNR;
    @FXML
    private CheckBox isTall;
    @FXML
    private CheckBox isDamaged;
    @FXML
    private ToggleGroup weight;
    @FXML
    private ChoiceBox<String> palletType;
    private final String[] palletTypeOptions = {"Europaleta", "Americká paleta", "GitterBox"};

    private final Map<String, Integer> materialMap = new LinkedHashMap<>();

    private static final int PAIR_HEIGHT = 25;

    private static final int MAX_PAIRS = 6;
    private int currentPairCount;
    private boolean isCountValid;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        PalletInformationDataSet dataSet = Warehouse.getInstance().getStoreInInstance().getPalletInformationDataSet();

        currentPairCount = 0;
        isCountValid = true;

        if (dataSet == null){
            palletType.getItems().addAll(palletTypeOptions);
            palletType.setValue("Europaleta");
            addMaterial();
        }
        else {
            setupValuesFromDataSet(dataSet);
        }

        Warehouse.getStage().setMinWidth(550);
        Warehouse.getStage().setMinHeight(550);

        Warehouse.getInstance().addController("palletInformation", this);
    }

    public void setupValuesFromDataSet(PalletInformationDataSet dataSet){
        PNR.setText(dataSet.PNR());
        isDamaged.setSelected(dataSet.isDamaged());
        isTall.setSelected(dataSet.isTall());
        materialMap.clear();
        materialMap.putAll(dataSet.materialMap());

        palletType.getItems().addAll(palletTypeOptions);
        palletType.setValue(dataSet.palletType());

        setWeight(dataSet.weight());
        materialContainer.getChildren().clear();
        setUpMaterialContainer(dataSet.materialMap());

    }

    public void setUpMaterialContainer(Map<String, Integer> materialMap){
        for (String name : materialMap.keySet()){
            HBox materialPair = createMaterialPair();
            ((TextField) materialPair.getChildren().get(1)).setText(name);
            ((TextField) materialPair.getChildren().get(3)).setText(String.valueOf(materialMap.get(name)));
            materialContainer.getChildren().add(materialPair);
            currentPairCount++;
            updateContainerHeights();
        }
        checkMaterialPairCount();
    }

    public void addMaterial() {
        if (currentPairCount < MAX_PAIRS && isCountValid) {
            if (getLastMaterialPair() == null || !isLastMaterialEmpty()) {
                errorMessage.setText("");
                currentPairCount++;

                materialContainer.getChildren().add(createMaterialPair());
                updateContainerHeights();
                checkMaterialPairCount();
            }
            else {
                errorMessage.setText("Nevyplnili ste materiál a jeho počet");
            }
        }
    }

    private void checkMaterialPairCount(){
        if (currentPairCount == MAX_PAIRS) {
            addMaterialButton.setDisable(true);
        }
    }

    private HBox createMaterialPair() {
        Label materialLabel = new Label("Materiál:");
        TextField materialTextField = new TextField();

        Label countLabel = new Label("Počet kusov:");
        TextField countTextField = new TextField();

        HBox materialPair = new HBox(materialLabel, materialTextField, countLabel, countTextField);

        if (materialContainer.getChildren().size() > 0) {
            Button removeButton = new Button("X");
            removeButton.setOnAction(event -> removeMaterialPair(removeButton));
            materialPair.getChildren().add(removeButton);
        }
        else {
            Label placeholder = new Label();
            placeholder.setMinWidth(23);
            materialPair.getChildren().add(placeholder);
        }

        materialPair.setSpacing(15);
        materialPair.setAlignment(Pos.CENTER);

        materialTextField.textProperty().addListener((observable, oldValue, newValue) ->
                updateMaterialMap(newValue, countTextField.getText()));

        countTextField.textProperty().addListener((observable, oldValue, newValue) -> handleCountTextField(materialTextField, newValue));

        return materialPair;
    }

    private void handleCountTextField(TextField materialTextField, String newValue) {
        try {
            int count = Integer.parseInt(newValue);
            if (count > 0) {
                errorMessage.setText("");
                isCountValid = true;
                updateMaterialMap(materialTextField.getText(), newValue);
            } else {
                errorMessage.setText("Počet kusov musí byť aspoň 1");
            }
        } catch (NumberFormatException e) {
            errorMessage.setText("Počet kusov musí byť číslo");
            isCountValid = false;
        }
    }

    private HBox getLastMaterialPair() {
        int size = materialContainer.getChildren().size();
        return size > 0 ? (HBox) materialContainer.getChildren().get(size - 1) : null;
    }

    private boolean isLastMaterialEmpty() {
        return ((TextField) Objects.requireNonNull(getLastMaterialPair()).getChildren().get(1)).getText().isEmpty() ||
                ((TextField) getLastMaterialPair().getChildren().get(3)).getText().isEmpty();
    }

    private void updateMaterialMap(String material, String count) {
        if (!material.isEmpty() && !count.isEmpty()) {
            materialMap.put(material, Integer.parseInt(count));
        }
    }

    private void removeMaterialPair(Button removeButton) {
        HBox materialPair = (HBox) removeButton.getParent();

        String materialName = ((TextField) materialPair.getChildren().get(1)).getText();
        materialMap.remove(materialName);

        if (materialContainer.getChildren().size() > 1) {
            materialContainer.getChildren().remove(materialPair);
            addMaterialButton.setDisable(false);
            currentPairCount--;
        }
        updateContainerHeights();
        if (!isLastMaterialEmpty()){
            errorMessage.setText("");
        }
    }

    private void updateContainerHeights() {
        materialContainer.setPrefHeight(currentPairCount * PAIR_HEIGHT);
        double newHeight = topContainer.getPrefHeight() + materialContainer.getPrefHeight() + informationContainer.getPrefHeight();

        borderPane.setPrefHeight(newHeight);
        Warehouse.getStage().setMinHeight(newHeight);
        Warehouse.getStage().setHeight(newHeight);
    }

    public void backToCustomerTruckNumberForm()throws IOException{
        Warehouse.getInstance().removeController("palletInformation");
        Warehouse.getInstance().getStoreInInstance().removePalletInformationDataSet();
        Warehouse.getInstance().changeScene("StoreInProduct/customerTruckNumberForm.fxml");
    }

    public void nextToPositionForm() throws IOException {
        if (materialMap.isEmpty()){
            errorMessage.setText("Nezadali ste materiál a jeho počet na zaskladnenie");
        }
        else if (isLastMaterialEmpty()) {
            errorMessage.setText("Nevyplnili ste posledný materiál a jeho počet");
        }
        else if (PNR.getText().isEmpty()) {
            errorMessage.setText("Nezadali ste PNR číslo");
        }
        else if (weight.getSelectedToggle() == null){
            errorMessage.setText("Nezadali ste hmotnosť");
        }
        else if (Warehouse.getInstance().getDatabaseHandler().PNRisUsed(PNR.getText())){
            errorMessage.setText("Zadali ste PNR, ktoré sa už používa");
        }
        else {
            try {
                int PNRnumber = Integer.parseInt(PNR.getText());
                if (PNRnumber < 2000 || PNRnumber > 3500){
                    errorMessage.setText("PNR musí byť v rozmedzí 2000-3500");
                }
                else {
                    Warehouse.getInstance().getStoreInInstance().initializePalletInformationDataSet(getPNR(),
                            getIsDamaged(), getIsTall(), getMaterialMap(), getPalletType(), getWeight());
                    Warehouse.getInstance().changeScene("StoreInProduct/storeInPositionForm.fxml");
                }
            }
            catch (NumberFormatException e){
                errorMessage.setText("PNR musí byť číslo");
            }
        }
    }

    public String getPNR() {
        return PNR.getText();
    }

    public boolean getIsDamaged() {
        return isDamaged.isSelected();
    }

    public boolean getIsTall() {
        return isTall.isSelected();
    }

    public Map<String, Integer> getMaterialMap() {
        return materialMap;
    }

    public String getPalletType() {
        return palletType.getValue();
    }

    public Integer getWeight(){
        RadioButton selectedRadioButton = (RadioButton) weight.getSelectedToggle();
        String weightID = selectedRadioButton.getId();
        if (weightID.equals("first")){
            return 500;
        }
        if (weightID.equals("second")){
            return 1000;
        }
        if (weightID.equals("third")){
            return 1200;
        }
        return 2000;
    }

    public void setWeight(Integer weightValue){
        if (weightValue == 500) {
            weight.getToggles().get(0).setSelected(true);
        }
        else if (weightValue == 1000) {
            weight.getToggles().get(1).setSelected(true);
        }
        else if (weightValue == 1200){
            weight.getToggles().get(2).setSelected(true);
        }
        else{
            weight.getToggles().get(3).setSelected(true);
        }
    }
}

