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
    private Label message;

    private final String WARNING = "Pokiaľ ostránite zákazníka, budú ostránené všetky informácie s ním spojené. " +
            "\nZáznamy o histórii daného zákazníka budú taktiež odstránené.";

    /***
     * Initializes the controller class and sets the warning message.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        message.setText(WARNING);
    }

    /***
     * Goes back to the customer management main menu.
     * @throws IOException if the scene cannot be loaded
     */
    public void backToCustomerManagement() throws IOException {
        Warehouse.getInstance().changeScene("CustomerManagement/customerManagementMain.fxml");
    }

    /***
     * Deletes the customer from the database and goes to the confirmation scene.
     * @throws IOException if the scene cannot be loaded
     */
    public void deleteCustomer() throws IOException {
        Warehouse.getInstance().addController("deleteCustomer", true);
        String customerName = (String) Warehouse.getInstance().getController("customerName");
        Warehouse.getInstance().getDatabaseHandler().deleteCustomer(customerName);
        Warehouse.getInstance().changeScene("CustomerManagement/customerConfirmation.fxml");
    }
}
