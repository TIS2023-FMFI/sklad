package GUI.OrderProduct;

import Entity.Customer;
import app.Warehouse;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class OrderCustomerSelectionController implements Initializable {

    @FXML
    private ChoiceBox<String> customerChoices;

    /***
     * Customer that was selected in the choice box
     */
    public Customer customer;

    @FXML
    private Label errorField;

    /***
     * Initializes the choice box with the customers from the database
     * @param arg0
     * @param arg1
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        ObservableList<String> customers = Warehouse.getInstance().getDatabaseHandler().getCustomersNames();
        customerChoices.setItems(customers);
        customerChoices.setValue(customers.get(0));
        Warehouse.getInstance().addController("OrderCustomerSelectionController", this);
    }

    /***
     * Goes back to the main menu
     * @throws IOException when the fxml file is not found
     */
    public void backToMenu() throws IOException {
       Warehouse.getInstance().changeScene("mainMenu.fxml");
    }

    /***
     * Confirms the selected customer and goes to the next scene
     * @throws IOException when the fxml file is not found
     */
    public void confirmOrderCustomer() throws IOException {
        if (customerChoices.getValue() == null) {
            errorField.setText("Vyberte zákazníka!");
            return;
        }
        customer = Warehouse.getInstance().getDatabaseHandler().getCustomer(customerChoices.getValue());
        if (customer != null) {
            Warehouse.getInstance().changeScene("OrderProduct/orderProductsForm.fxml");
        }
    }
}
