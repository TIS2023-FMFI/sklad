package GUI.OrderProduct;

import Entity.Customer;
import app.Warehouse;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class OrderCustomerSelectionController implements Initializable {

    @FXML
    public ChoiceBox customerChoices;

    public Customer customer;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        List<Customer> cust = Warehouse.getInstance().getDatabaseHandler().getCustomers();
        for (Customer c : cust) {
            customerChoices.getItems().add(c.getName());
        }
        Warehouse.getInstance().addController("OrderCustomerSelectionController", this);
    }

    public void backToMenu() throws IOException {
       Warehouse.getInstance().changeScene("mainMenu.fxml");
    }

    public void confirmOrderCustomer() throws IOException {
        customer = Warehouse.getInstance().getDatabaseHandler().getCustomer(customerChoices.getValue().toString());
        if (customer != null) {
            Warehouse.getInstance().changeScene("OrderProduct/orderProductsForm.fxml");
        }
    }
}
