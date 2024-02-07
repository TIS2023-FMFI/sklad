package GUI.OrderProduct;

import Entity.Customer;
import app.OrderProduct;
import app.Warehouse;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class OrderMaterialChoices implements Initializable {
    @FXML
    private TableView choicesOfProduct;
    @FXML
    private Label header;
    private static final String STYLE = "-fx-font: 17px 'Calibri'; -fx-alignment: CENTER;";
    @FXML
    private Label errorLabel;
    private ObservableList<Map<String, Object>> items = FXCollections.observableArrayList();
    private OrderProductsController cont;
    private String materialName;

    /***
     * Initializes the controller and sets up the table columns
     * @param arg0
     * @param arg1
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        cont = (OrderProductsController) Warehouse.getInstance().getController("OrderProductsController") ;
        Customer customer = cont.customer;
        materialName = cont.material.getValue();
        header.setText("Výber materiálu: " + materialName);


        TableColumn<Map, String> positionColumn = new TableColumn<>("Pozícia");
        positionColumn.setCellValueFactory(new MapValueFactory<>("position"));
        positionColumn.setStyle(STYLE);

        TableColumn<Map, String> palletColumn = new TableColumn<>("Paleta:");
        palletColumn.setCellValueFactory(new MapValueFactory<>("pallet"));
        palletColumn.setStyle(STYLE);

        TableColumn<Map, Integer> maxNumberColumn = new TableColumn<>("Max:");
        maxNumberColumn.setCellValueFactory(new MapValueFactory<>("maxNumber"));
        maxNumberColumn.setStyle(STYLE);

        TableColumn<Map, String> dateColumn = new TableColumn<>("Dátum zaskladnenia:");
        dateColumn.setCellValueFactory(new MapValueFactory<>("date"));
        dateColumn.setStyle(STYLE);

        TableColumn<Map, Integer> quantityChoiceColumn = new TableColumn<>("Počet:");
        quantityChoiceColumn.setCellValueFactory(new MapValueFactory<>("quantity"));
        quantityChoiceColumn.setStyle(STYLE);

        choicesOfProduct.getColumns().addAll(positionColumn, palletColumn, maxNumberColumn, dateColumn, quantityChoiceColumn);

        OrderProduct op = new OrderProduct();
        items.addAll(op.setMaterialChoiceTable(customer, materialName));
        choicesOfProduct.getItems().addAll(items);

    }

    /***
     * Confirms the adding of the selected items to the order
     */
    public void confirmAdding() {
        ObservableList<Map<String, String>> newItems = FXCollections.observableArrayList();
        for (Map<String, Object> item : items) {
            int quantity = getAddedQuantity(item);
            if (quantity == -1) {
                return;
            } else if (quantity == 0) {
                continue;
            }
            Map<String,String> newItem = Map.of(
                    "Pozícia", (String) item.get("position"),
                    "Paleta", (String) item.get("pallet"),
                    "Počet", String.valueOf(quantity),
                    "Materiál", materialName);

            newItems.add(newItem);
        }
        cont.items.addAll(newItems);
        cont.updateTable();
    }

    private int getAddedQuantity(Map<String, Object> item) {
        var sp = (Spinner<Integer>) item.get("quantity");
        int quantity = sp.getValue();
        for (Map<String, String> i : cont.items) {
            if (i.get("Paleta").equals(item.get("pallet")) && i.get("Materiál").equals(materialName)){
                quantity += Integer.parseInt(i.get("Počet"));
                if (quantity > (int) item.get("maxNumber")) {
                    errorLabel.setText("Nemožno pridať viac kusov ako je na sklade");
                    return -1;
                }
                cont.items.remove(i);
                break;
            }
        }
        return quantity;
    }
}
