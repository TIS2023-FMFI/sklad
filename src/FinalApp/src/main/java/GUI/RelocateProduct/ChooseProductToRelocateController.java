package GUI.RelocateProduct;
import Entity.Position;
import app.DatabaseHandler;
import app.Warehouse;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ChooseProductToRelocateController implements Initializable {
    @FXML
    private TextField quantityText;
    @FXML
    private Label errorLabel;
    @FXML
    private Label materialToMoveLabel;
    @FXML
    private ChoiceBox<String> productsOnPallet;
    private Map<String, Integer> productsOnPalletMap = new HashMap<>();
    /***
     * Chosen position from which the product will be moved
     */
    public Position initialPosition;
    /***
     * Flag, if we are moving the whole pallet or just one material
     */
    public boolean isWholePallet;
    /***
     * If we are moving one material, this is the material
     */
    public String finalMaterial = null;
    /***
     * If we are moving one material, this is the quantity
     */
    public Integer finalQuantity = null;
    /***
     * If we are moving the material, this is the pallet, to which the material will go
     */
    public String finalPallet = null;

    /***
     * Method that initializes the controller and disables the quantity textfield if we are moving the whole pallet
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MoveProductFromPositionController controller = (MoveProductFromPositionController)
                Warehouse.getInstance().getController("MoveProductFromPositionController");
        String position = controller.positionName;
        isWholePallet = controller.isWholePallet;
        if (isWholePallet){
            materialToMoveLabel.setText("Vyberte paletu na preskladnenie:");
            quantityText.setDisable(true);
            fillPalletsOnPosition(position);
        }else {
            fillProductsAndPallets(position);
        }
    }

    private void fillProductsAndPallets(String position){
        //fills productsOnPallet with products on position
        DatabaseHandler databaseHandler = Warehouse.getInstance().getDatabaseHandler();
        Position pos = databaseHandler.getPosition(position);
        initialPosition = pos;
        var palletsOnPos = Warehouse.getInstance().getPalletsOnPositionMap().get(pos);
        for (var pallet : palletsOnPos.keySet()){
            for (var material : palletsOnPos.get(pallet).keySet()){
                int quantity = palletsOnPos.get(pallet).get(material);
                StringBuffer sb = new StringBuffer();
                sb.append(pallet.getPnr());
                sb.append(": ");
                sb.append(material.getName());
                productsOnPalletMap.put(sb.toString(), quantity);
                sb.append(" (max. ");
                sb.append(quantity);
                sb.append(")");
                productsOnPallet.getItems().add(sb.toString());
            }
        }
    }

    private void fillPalletsOnPosition(String position){
        //fills productsOnPallet with pallets on position
        DatabaseHandler databaseHandler = Warehouse.getInstance().getDatabaseHandler();
        Position pos = databaseHandler.getPosition(position);
        initialPosition = pos;
        var palletsOnPos = Warehouse.getInstance().getPalletsOnPositionMap().get(pos);
        for (var pallet : palletsOnPos.keySet()){
            String sb = pallet.getPnr();
            productsOnPallet.getItems().add(sb);
        }
    }

    /***
     * Method that takes user back to choosing the initial position
     * @throws IOException if the fxml file is not found
     */
    public void backToInitialPosition() throws IOException {
        Warehouse.getInstance().changeScene("RelocateProduct/moveProductFromPositionForm.fxml");
    }

    /***
     * Method that confirms the product to move and takes user to the form where he chooses the final position
     * @throws IOException if the fxml file is not found
     */
    public void confirmProductToMove() throws IOException {
        if (!isWholePallet) {
            if (productsOnPallet.getValue() == null){
                errorLabel.setText("Vyberte produkt na preskladnenie.");
                return;
            }
            if (quantityText.getText().isEmpty()) {
                errorLabel.setText("Zadajte množstvo produktu.");
                return;
            }
            int num;
            try {
                num = Integer.parseInt(quantityText.getText());
            } catch (NumberFormatException e) {
                errorLabel.setText("Množstvo produktu musí byť číslo.");
                return;
            }
            if (num <= 0) {
                errorLabel.setText("Množstvo produktu musí byť väčšie ako 0.");
                return;
            }
            String key = productsOnPallet.getValue().substring(0, productsOnPallet.getValue().indexOf(" ("));
            if (num > productsOnPalletMap.get(key)) {
                errorLabel.setText("Zadané množstvo je väčšie ako množstvo produktu na palete.");
                return;
            }
            String finalString = productsOnPallet.getValue();
            finalQuantity = Integer.parseInt(quantityText.getText());
            finalPallet = finalString.substring(0, finalString.indexOf(":"));
            finalMaterial = finalString.substring(finalString.indexOf(":") + 2, finalString.indexOf(" (max. "));

        }else{
            if (productsOnPallet.getValue() == null){
                errorLabel.setText("Vyberte paletu na preskladnenie");
                return;
            }
            finalPallet = productsOnPallet.getValue();;
        }

        Warehouse.getInstance().addController("ChooseProductToRelocateController", this);
        Warehouse.getInstance().changeScene("RelocateProduct/moveProductToPositionForm.fxml");
    }
}
