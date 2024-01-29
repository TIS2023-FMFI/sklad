package GUI.OrderProduct;

import Entity.Customer;
import Entity.Material;
import Exceptions.MaterialNotAvailable;
import app.FileExporter;
import app.OrderProduct;
import app.Warehouse;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class OrderShowPositionsController implements Initializable {
    @FXML
    private TableView orderTable;

    protected ObservableList<Map<String, String>> items = FXCollections.observableArrayList();

    /***
     * Customer for which the order is being made
     */
    Customer cust;

    /***
     * Initializes the table with the ordered products
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        OrderProductsController cont = (OrderProductsController) Warehouse.getInstance().
                getController("OrderProductsController");

        TableColumn<Map, String> materialColumn = new TableColumn<>("Materiál");
        materialColumn.setCellValueFactory(new MapValueFactory<>("Materiál"));

        TableColumn<Map, String> quantityColumn = new TableColumn<>("Počet");
        quantityColumn.setCellValueFactory(new MapValueFactory<>("Počet"));

        TableColumn<Map, String>positionColumn = new TableColumn<>("Pozícia");
        positionColumn.setCellValueFactory(new MapValueFactory<>("Pozícia"));

        TableColumn<Map, String> PNRColumn = new TableColumn<>("PNR");
        PNRColumn.setCellValueFactory(new MapValueFactory<>("PNR"));

        orderTable.getColumns().addAll(materialColumn, quantityColumn, positionColumn, PNRColumn);

        List<Pair<Material,Integer>> products = cont.materials;
        Customer customer = cont.customer;
        cust = cont.customer;
        OrderProduct op = new OrderProduct();

        try {
            items.addAll(op.setOrderTable(customer, products));
        } catch (MaterialNotAvailable e) {
            try {
                Warehouse.getInstance().changeScene("OrderProduct/orderProductsForm.fxml");
                ((OrderProductsController) Warehouse.getInstance().getController("OrderProductsController")).
                        errorMessage.setText(e.getMessage());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            e.printStackTrace();
        }

        orderTable.getItems().addAll(items);

    }

    /***
     * Goes back to the customer selection form
     * @throws IOException if the form is not found
     */
    public void backToCustomerSelection() throws IOException {
        Warehouse.getInstance().changeScene("OrderProduct/orderCustomerSelectionForm.fxml");
    }

    /***
     * Exports the order to an excel file
     */
    public void saveOrderExcel(){
        FileExporter fe = new FileExporter();
        List<String> columns = new ArrayList<>();
        columns.add("Materiál");
        columns.add("Počet");
        columns.add("Pozícia");
        columns.add("PNR");
        fe.exportExcel(items, "Order", "Objednávka", columns);

    }

    /***
     * Exports the order to a pdf file
     */
    public void saveOrdersPdf(){
        FileExporter fe = new FileExporter();
        fe.exportOrderPDF(items, cust);
    }

    /***
     * Goes to the confirmation form
     * @throws IOException if the form is not found
     */
    public void continueToConfirmation() throws IOException {
        OrderProduct op = new OrderProduct();
        op.removeOrderedItems(items);
        Warehouse.getInstance().addController("OrderShowPositionsController", this);
        Warehouse.getInstance().changeScene("OrderProduct/OrderDownloadConfirmation.fxml");
    }
}
