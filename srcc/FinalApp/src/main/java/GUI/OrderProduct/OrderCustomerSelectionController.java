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
    public ChoiceBox<String> customerChoices;

    public Customer customer;
    public Label errorField;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        ObservableList<String> customers = Warehouse.getInstance().getDatabaseHandler().getCustomersNames();
        customerChoices.setItems(customers);
        customerChoices.setValue(customers.get(0));
        Warehouse.getInstance().addController("OrderCustomerSelectionController", this);
    }

    public void backToMenu() throws IOException {
       Warehouse.getInstance().changeScene("mainMenu.fxml");
    }

    public void confirmOrderCustomer() throws IOException {
        if (customerChoices.getValue() == null) {
            errorField.setText("Vyberte zákazníka!");
            return;
        }
        customer = Warehouse.getInstance().getDatabaseHandler().getCustomer(customerChoices.getValue().toString());
        if (customer != null) {
            Warehouse.getInstance().changeScene("OrderProduct/orderProductsForm.fxml");
        }
    }
}
