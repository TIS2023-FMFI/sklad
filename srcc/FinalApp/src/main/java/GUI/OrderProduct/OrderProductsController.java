package GUI.OrderProduct;

import Entity.Customer;
import Entity.Material;
import Exceptions.MaterialNotAvailable;
import app.OrderProduct;
import app.Warehouse;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class OrderProductsController implements Initializable {
    @FXML
    public ChoiceBox<String> material;
    @FXML
    public TextField quantity;
    @FXML
    public Label addedMaterials;

    public List<Pair<Material,Integer>> materials = new ArrayList<>();
    @FXML
    public Label errorMessage;

    public Customer customer;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        Warehouse.getInstance().addController("OrderProductsController", this);
        OrderCustomerSelectionController cont = (OrderCustomerSelectionController) Warehouse.getInstance().
                getController("OrderCustomerSelectionController");
        customer = cont.customer;
        var dbh = Warehouse.getInstance().getDatabaseHandler();
        var lm = dbh.getMaterials(customer);
        for (String m : lm) {
            material.getItems().add(m);
        }
    }

    public void addProduct() {
        try {
            if (material.getValue() == null) {
                errorMessage.setText("Vyberte materiál!");
                return;
            }
            Material m = Warehouse.getInstance().getDatabaseHandler().getMaterial(material.getValue());
            if (quantity.getText().length() == 0) {
                errorMessage.setText("Zadajte počet!");
                return;
            }
            Pair<Material, Integer> oldPair = null;
            var newMaterial = new Pair<>(m, Integer.parseInt(quantity.getText()));
            for (Pair<Material,Integer> p : materials) {
                if (p.getKey().getName().equals(m.getName())) {
                    oldPair = p;
                    newMaterial = new Pair<>(m, p.getValue() + Integer.parseInt(quantity.getText()));
                    materials.remove(p);
                    break;
                }
            }

            OrderProduct op = new OrderProduct();
            if (!op.canCustomerOrder(customer, m, newMaterial.getValue())) {
                errorMessage.setText("Nedostatok materiálu!");
                return;
            }

            materials.add(newMaterial);
            writeMaterials();
        } catch (MaterialNotAvailable e) {
            errorMessage.setText(e.getMessage());
        } catch (NumberFormatException e){
            errorMessage.setText("Počet musí byť číslo!");
        }
    }

    private void writeMaterials() {
        StringBuilder text = new StringBuilder();
        for (Pair<Material,Integer> p  : materials) {
            text.append(p.getValue()).append("x ").append(p.getKey().getName()).append("; ");
        }
        addedMaterials.setText(text.toString());
    }

    public void backToCustomerSelection() throws IOException {
        Warehouse.getInstance().changeScene("OrderProduct/orderCustomerSelectionForm.fxml");
    }
    public void confirmOrderProducts() throws IOException {
        Warehouse.getInstance().changeScene("OrderProduct/orderShowPositionsForm.fxml");
    }

    public void clearMaterials() {
        materials.clear();
        writeMaterials();
    }
}
