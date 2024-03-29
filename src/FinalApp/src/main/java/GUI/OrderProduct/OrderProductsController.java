package GUI.OrderProduct;

import Entity.Customer;
import Entity.Material;
import Exceptions.MaterialNotAvailable;
import app.CheckPositions;
import app.OrderProduct;
import app.Warehouse;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class OrderProductsController implements Initializable {
    @FXML
    protected ChoiceBox<String> material;
    @FXML
    private Label errorMessage;

    public Customer customer;
    @FXML
    protected TableView alreadyChosenMaterials;
    protected ObservableList<Map<String, String>> items = FXCollections.observableArrayList();

    private Stage newStage;

    private static final String STYLE = "-fx-font: 17px 'Calibri'; -fx-alignment: CENTER;";

    /***
     * Initializes the table with the correct columns.
     * @param arg0
     * @param arg1
     */
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

        TableColumn<Map, String> positionColumn = new TableColumn<>("Pozícia");
        positionColumn.setCellValueFactory(new MapValueFactory<>("Pozícia"));
        positionColumn.setStyle(STYLE);

        TableColumn<Map, String> palletColumn = new TableColumn<>("Paleta");
        palletColumn.setCellValueFactory(new MapValueFactory<>("Paleta"));
        palletColumn.setStyle(STYLE);

        TableColumn<Map, String> matColumn = new TableColumn<>("Materiál");
        matColumn.setCellValueFactory(new MapValueFactory<>("Materiál"));
        matColumn.setStyle(STYLE);

        TableColumn<Map, Integer> numberColumn = new TableColumn<>("Počet");
        numberColumn.setCellValueFactory(new MapValueFactory<>("Počet"));
        numberColumn.setStyle(STYLE);

        alreadyChosenMaterials.getColumns().addAll(positionColumn, palletColumn, matColumn, numberColumn);

    }

    /***
     * Adds the product to the table of chosen products
     */
    public void addProduct() {
        if (material.getValue() == null) {
            errorMessage.setText("Vyberte materiál!");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(CheckPositions.class.getResource("OrderProduct/orderChoiceProductsPopUp.fxml"));
            Parent root = loader.load();

            newStage = new Stage();
            newStage.setTitle("Možnosti výberu materiálu");
            newStage.setScene(new Scene(root));

            newStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /***
     * Takes user back to the customer selection form
     * @throws IOException if the scene is not found
     */
    public void backToCustomerSelection() throws IOException {
        Warehouse.getInstance().changeScene("OrderProduct/orderCustomerSelectionForm.fxml");
    }

    /***
     * Confirms the order and takes user to the order show positions form
     * @throws IOException if the scene is not found
     */
    public void confirmOrderProducts() throws IOException {
        if (items.size() == 0) {
            errorMessage.setText("Musíte pridať aspoň jeden materiál!");
            return;
        }
        Warehouse.getInstance().changeScene("OrderProduct/orderShowPositionsForm.fxml");
    }

    /***
     * Removes the selected products from the table
     */
    public void clearMaterials() {
        alreadyChosenMaterials.getItems().clear();
        items.clear();
    }

    /***
     * Adds the new items to the table.
     */
    public void updateTable() {
        alreadyChosenMaterials.getItems().clear();
        alreadyChosenMaterials.getItems().addAll(items);
        newStage.close();
    }
}
