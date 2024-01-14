package GUI.RelocateProduct;
import Entity.Pallet;
import Entity.Position;
import app.DatabaseHandler;
import app.OrderProduct;
import app.RelocateProduct;
import app.Warehouse;
import jakarta.persistence.criteria.CriteriaBuilder;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ChooseProductToRelocateController implements Initializable {
    @FXML
    public TextField quantityText;
    @FXML
    private ChoiceBox<String> productsOnPallet;

    private Map<String, Integer> productsOnPalletMap = new HashMap<>();

    public String finalMaterial;
    public int finalQuantity;
    public String finalPallet;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MoveProductFromPositionController controller = (MoveProductFromPositionController)
                Warehouse.getInstance().getController("MoveProductFromPositionController");
        String position = controller.positionName;
        fillProductsOnPallet(position);
    }

    private void fillProductsOnPallet(String position){
        //fills productsOnPallet with products on position
        DatabaseHandler databaseHandler = Warehouse.getInstance().getDatabaseHandler();
        Position pos = databaseHandler.getPosition(position);
        var palletsOnPos = Warehouse.getInstance().getPalletsOnPosition().get(pos);
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

    public void backToInitialPosition() throws IOException {
        Warehouse.getInstance().changeScene("RelocateProduct/moveProductFromPositionForm.fxml");
    }
    public void confirmProductToMove() throws IOException {
        if (quantityText.getText().isEmpty()){
            System.out.println("Quantity is empty");
            return;
        }
        int num;
        try {
            num = Integer.parseInt(quantityText.getText());
        } catch (NumberFormatException e){
            System.out.println("Quantity is not a number");
            return;
        }
        String key = productsOnPallet.getValue().substring(0, productsOnPallet.getValue().indexOf(" ("));
        if (num > productsOnPalletMap.get(key)){
            System.out.println("Quantity is bigger than max quantity");
            return;
        }
        String finalString = productsOnPallet.getValue();
        finalQuantity = Integer.parseInt(quantityText.getText());
        finalPallet = finalString.substring(0, finalString.indexOf(":"));
        finalMaterial = finalString.substring(finalString.indexOf(":") + 2, finalString.indexOf(" (max. "));
        Warehouse.getInstance().changeScene("RelocateProduct/moveProductToPositionForm.fxml");
    }
}
