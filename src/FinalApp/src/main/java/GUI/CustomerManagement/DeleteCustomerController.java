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

public class DeleteCustomerController implements Initializable {

    @FXML
    public Label message;

    private final String WARNING = "Pokiaľ ostránite zákazníka, budú ostránené všetky informácie s ním spojené.\n" +
            "Záznamy o histórii daného zákazníka budú taktiež odstráné.";
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        message.setText(WARNING);
    }

    public void backToCustomerManagement() throws IOException {
        Warehouse.getInstance().changeScene("CustomerManagement/customerManagementMain.fxml");
    }

    public void deleteCustomer() throws IOException {
        Warehouse.getInstance().addController("deleteCustomer", true);
        String customerName = ((ChoiceBox<String>) Warehouse.getInstance().getController("customerName")).getValue();
        Warehouse.getInstance().getDatabaseHandler().deleteCustomer(customerName);
        Warehouse.getInstance().changeScene("CustomerManagement/customerConfirmation.fxml");
    }
}
