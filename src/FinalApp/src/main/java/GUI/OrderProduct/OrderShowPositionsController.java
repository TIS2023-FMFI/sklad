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
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class OrderShowPositionsController implements Initializable {
    @FXML
    private TableView orderTable;

    protected ObservableList<Map<String, String>> items = FXCollections.observableArrayList();
    private static final String STYLE = "-fx-font: 17px 'Calibri'; -fx-alignment: CENTER;";

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
        materialColumn.setPrefWidth(150);

        TableColumn<Map, String> quantityColumn = new TableColumn<>("Počet");
        quantityColumn.setCellValueFactory(new MapValueFactory<>("Počet"));
        quantityColumn.setPrefWidth(74);

        TableColumn<Map, String> positionColumn = new TableColumn<>("Pozícia");
        positionColumn.setCellValueFactory(new MapValueFactory<>("Pozícia"));
        positionColumn.setPrefWidth(200);

        TableColumn<Map, String> PNRColumn = new TableColumn<>("Paleta");
        PNRColumn.setCellValueFactory(new MapValueFactory<>("Paleta"));
        PNRColumn.setPrefWidth(74);

        orderTable.getColumns().addAll(materialColumn, quantityColumn, positionColumn, PNRColumn);
        cust = cont.customer;

        items.addAll(cont.items);



        orderTable.getItems().addAll(cont.items);

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
        columns.add("Paleta");
        System.out.println(items);
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
