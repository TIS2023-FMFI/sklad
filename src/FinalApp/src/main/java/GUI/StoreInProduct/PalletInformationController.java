package GUI.StoreInProduct;

import Exceptions.ValidationException;
import app.Warehouse;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

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
    private TextField weight;
    @FXML
    private CheckBox isTall;
    @FXML
    private CheckBox isDamaged;
    @FXML
    private ToggleGroup numberOfPositions;
    @FXML
    private ChoiceBox<String> palletType;
    private final String[] palletTypeOptions = {"Europaleta", "Americká paleta", "GitterBox"};

    private final Map<String, Integer> materialMap = new LinkedHashMap<>();

    private static final int PAIR_HEIGHT = 30;

    private static final int MAX_PAIRS = 6;
    private static final int MATERIAL_CONTAINER_SPACING = 5;
    private int currentPairCount;
    private boolean isCountValid;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        PalletInformationDataSet dataSet = Warehouse.getInstance().getStoreInInstance().getPalletInformationDataSet();

        currentPairCount = 0;
        isCountValid = true;
        materialContainer.setSpacing(MATERIAL_CONTAINER_SPACING);

        if (dataSet == null){
            palletType.getItems().addAll(palletTypeOptions);
            palletType.setValue("Europaleta");
            addMaterial();
        }
        else {
            setupValuesFromDataSet(dataSet);
        }
        Warehouse.getInstance().addController("palletInformation", this);
    }
    public void setupValuesFromDataSet(PalletInformationDataSet dataSet){
        PNR.setText(dataSet.PNR());
        weight.setText(String.valueOf(dataSet.weight()));
        isDamaged.setSelected(dataSet.isDamaged());
        isTall.setSelected(dataSet.isTall());
        materialMap.clear();
        materialMap.putAll(dataSet.materialMap());

        palletType.getItems().addAll(palletTypeOptions);
        palletType.setValue(dataSet.palletType());

        setNumberOfPositions(dataSet.numberOfPositions());
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
        Warehouse warehouse = Warehouse.getInstance();
        Label materialLabel = warehouse.createStyledLabel("Materiál:", 20);
        TextField materialTextField = warehouse.createTextfield(150, 30);
        materialTextField.setStyle("-fx-font-size: 16;");

        Label countLabel = warehouse.createStyledLabel("Počet kusov:", 20);
        TextField countTextField = warehouse.createTextfield(150, 30);
        countTextField.setStyle("-fx-font-size: 16;");

        HBox materialPair = new HBox(materialLabel, materialTextField);

        HBox.setMargin(materialTextField, new Insets(0, 50, 0, 0));

        materialPair.getChildren().addAll(countLabel, countTextField);

        if (materialContainer.getChildren().size() > 0) {
            JFXButton removeButton = warehouse.createStyledButton("X", "#B80F0A",
                    "#FFFFFF", 29, 29, 17, true);
            removeButton.setAlignment(Pos.CENTER);
            removeButton.setOnAction(event -> removeMaterialPair(removeButton));
            materialPair.getChildren().add(removeButton);
        }
        else {
            Label placeholder = new Label();
            placeholder.setMinWidth(32);
            materialPair.getChildren().add(placeholder);
        }

        materialPair.setSpacing(20);
        materialPair.setAlignment(Pos.CENTER);

        countTextField.textProperty().addListener((observable, oldValue, newValue) -> handleCountTextField(newValue));

        return materialPair;
    }

    private void handleCountTextField(String newValue) {
        try {
            int count = Integer.parseInt(newValue);
            if (count > 0) {
                errorMessage.setText("");
                isCountValid = true;
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

    private void updateMaterialMap() {
        materialMap.clear();
        for (Node materialPairNode : materialContainer.getChildren()) {
            if (materialPairNode instanceof HBox) {
                HBox materialPairHBox = (HBox) materialPairNode;

                String material = ((TextField) materialPairHBox.getChildren().get(1)).getText();
                int count = Integer.parseInt(((TextField) materialPairHBox.getChildren().get(3)).getText());
                materialMap.put(material, count);
            }
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
        materialContainer.setPrefHeight(currentPairCount * (PAIR_HEIGHT + MATERIAL_CONTAINER_SPACING));
        double newHeight = topContainer.getPrefHeight() + materialContainer.getPrefHeight() + informationContainer.getPrefHeight();

        borderPane.setPrefHeight(newHeight);
        Warehouse.getStage().setMinHeight(newHeight);
        Warehouse.getStage().setHeight(newHeight);
    }

    public void backToCustomerTruckNumberForm()throws IOException{
        Warehouse warehouse = Warehouse.getInstance();

        warehouse.removeController("palletInformation");
        warehouse.getStoreInInstance().removePalletInformationDataSet();
        warehouse.changeScene("StoreInProduct/customerTruckNumberForm.fxml");
    }

    public void nextToPositionForm() throws IOException {
        if (isLastMaterialEmpty()) {
            errorMessage.setText("Nevyplnili ste posledný materiál a jeho počet");
        }
        else if (PNR.getText().isEmpty()) {
            errorMessage.setText("Nezadali ste PNR číslo");
        }
        else if (weight.getText().isEmpty()) {
            errorMessage.setText("Nezadali ste hmotnosť palety");
        }
        else if (numberOfPositions.getSelectedToggle() == null) {
            errorMessage.setText("Nezadali ste hmotnosť");
        }
        else if (Warehouse.getInstance().getDatabaseHandler().PNRisUsed(PNR.getText())) {
            errorMessage.setText("Zadali ste PNR, ktoré sa už používa");
        }
        else {
            try {
                validatePNR();
                validateWeight();

                updateMaterialMap();
                Warehouse.getInstance().getStoreInInstance().initializePalletInformationDataSet(getPNR(), getWeight(),
                        getIsDamaged(), getIsTall(), getMaterialMap(), getPalletType(), getNumberOfPositions());
                Warehouse.getInstance().changeScene("StoreInProduct/storeInPositionForm.fxml");
            } catch (ValidationException e) {
                errorMessage.setText(e.getMessage());
            }
        }
    }

    public void validateWeight() throws ValidationException {
        try {
            double weightValue = getWeight();
            if (weightValue <= 0) {
                throw new ValidationException("Hmotnosť musí byť číslo väčšie ako 0");
            }
        } catch (NumberFormatException e) {
            throw new ValidationException("Hmotnosť musí byť číslo");
        }
    }

    public void validatePNR() throws ValidationException {
        try {
            int PNRnumber = Integer.parseInt(PNR.getText());
            if (PNRnumber < 2000 || PNRnumber > 3500) {
                throw new ValidationException("PNR musí byť v rozmedzí 2000-3500");
            }
        }
        catch (NumberFormatException e) {
            throw new ValidationException("PNR musí byť číslo");
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

    public double getWeight() {
        return Double.parseDouble(weight.getText());

    }

    public int getNumberOfPositions(){
        RadioButton selectedRadioButton = (RadioButton) numberOfPositions.getSelectedToggle();
        String positionId = selectedRadioButton.getId();
        if (positionId.equals("onePosition")){
            return 1;
        }
        if (positionId.equals("twoPositions")){
            return 2;
        }
        if (positionId.equals("threePositions")){
            return 3;
        }
        return 4;
    }

    public void setNumberOfPositions(int number){
        if (number == 1) {
            numberOfPositions.getToggles().get(0).setSelected(true);
        }
        else if (number == 2) {
            numberOfPositions.getToggles().get(1).setSelected(true);
        }
        else if (number == 3){
            numberOfPositions.getToggles().get(2).setSelected(true);
        }
        else{
            numberOfPositions.getToggles().get(3).setSelected(true);
        }
    }
}

