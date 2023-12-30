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
    private ChoiceBox<String> palletType;
    private final String[] palletTypeOptions = {"Europaleta", "Americká paleta", "GitterBox"};

    private Map<String, Integer> materialMap = new LinkedHashMap<>();

    private static final int PAIR_HEIGHT = 25;

    private static final int MAX_PAIRS = 6;
    private int currentPairCount = 0;
    private boolean isCountValid = true;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        palletType.getItems().addAll(palletTypeOptions);
        palletType.setValue("Europaleta");
        addMaterial();
    }

    public void addMaterial() {
        if (currentPairCount < MAX_PAIRS && isCountValid) {
            HBox lastMaterialPair = getLastMaterialPair();

            if (lastMaterialPair == null || (
                    !((TextField) lastMaterialPair.getChildren().get(1)).getText().isEmpty() &&
                    !((TextField) lastMaterialPair.getChildren().get(3)).getText().isEmpty())) {

            errorMessage.setText("");
            currentPairCount++;

            Label materialLabel = new Label("Materiál:");
            TextField materialTextField = new TextField();

            Label countLabel = new Label("Počet kusov:");
            TextField countTextField = new TextField();

            HBox materialPair = new HBox(materialLabel, materialTextField, countLabel, countTextField);

            Label placeholder = new Label();
            placeholder.setMinWidth(23);

            if (materialContainer.getChildren().size() > 0) {
                Button removeButton = new Button("X");
                removeButton.setOnAction(event -> removeMaterialPair(removeButton));
                materialPair.getChildren().add(removeButton);
            }
            else {
                materialPair.getChildren().add(placeholder);
            }

            materialPair.setSpacing(15);

            materialContainer.getChildren().add(materialPair);
            materialPair.setAlignment(Pos.CENTER);

            if (currentPairCount == MAX_PAIRS) {
                addMaterialButton.setDisable(true);
            }

            materialTextField.textProperty().addListener((observable, oldValue, newValue) ->
                    updateMaterialMap(newValue, countTextField.getText()));
                countTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                    try {
                        int count = Integer.parseInt(newValue);
                        errorMessage.setText("");
                        isCountValid = true;
                        updateMaterialMap(materialTextField.getText(), newValue);
                    } catch (NumberFormatException e) {
                        errorMessage.setText("Počet kusov musí byť číslo");
                        isCountValid = false;
                    }
                });
            updateContainerHeights();
            }
            else {
                errorMessage.setText("Nevyplnili ste materiál a jeho počet");
            }
        }
    }
    private HBox getLastMaterialPair() {
        int size = materialContainer.getChildren().size();
        return size > 0 ? (HBox) materialContainer.getChildren().get(size - 1) : null;
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
        HBox lastMaterialPair = getLastMaterialPair();
        if ((!((TextField) lastMaterialPair.getChildren().get(1)).getText().isEmpty() &&
                !((TextField) lastMaterialPair.getChildren().get(3)).getText().isEmpty())){
            errorMessage.setText("");
        }
    }

    private void updateContainerHeights() {
        materialContainer.setPrefHeight(currentPairCount * PAIR_HEIGHT);
        double newHeight = topContainer.getPrefHeight() + materialContainer.getPrefHeight() + informationContainer.getPrefHeight();

        borderPane.setPrefHeight(newHeight);
        Warehouse.getStage().setHeight(newHeight);
    }

    public void backToCustomerTockaForm()throws IOException{
        Warehouse.getInstance().changeScene("StoreInProduct/customerTruckNumberForm.fxml");
    }

    public void nextToPositionForm() throws IOException {
        Warehouse.getInstance().changeScene("StoreInProduct/storeInPositionForm.fxml");
    }
}

