package GUI.CustomerManagement;

import app.Warehouse;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CustomerManagementMainController implements Initializable {
    @FXML
    public ChoiceBox<String> customer;
    @FXML
    public Label errorMessage;

    private final String NO_CUSTOMER = "Nie je vybrany žiadny zákazník. Najskôr vytvorte zákaníka.";
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> customers = Warehouse.getInstance().getDatabaseHandler().getCustomersNames();
        customer.setItems(customers);
        if(customers.size() > 0) {
            customer.setValue(customers.get(0));
        }
        if(Warehouse.getInstance().getController("customerName") != null){
            Warehouse.getInstance().removeController("customerName");
        }
    }

    public void saveCustomerName(){
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.addController("customerName", customer);
    }
    public void showInformation() throws IOException {
        if(customer.getValue() == null){
            errorMessage.setText(NO_CUSTOMER);
            return;
        }
        Warehouse warehouse = Warehouse.getInstance();
        if(warehouse.getController("customerName") != null){
            warehouse.removeController("customerName");
        }
        warehouse.addController("customerName", customer);
        Warehouse.getInstance().changeScene("CustomerManagement/createNewCustomer.fxml");
    }

    public void backToMainReservation() throws IOException {
        Warehouse.getInstance().changeScene("Reservations/reservationsMain.fxml");
    }
    public void createNewCustomerScene() throws IOException {
        Warehouse.getInstance().changeScene("CustomerManagement/createNewCustomer.fxml");
    }
}