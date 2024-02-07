package GUI.CustomerManagement;

import app.Warehouse;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CustomerConfirmationController implements Initializable {
    @FXML
    private Label message;
    private final String DATE_SAVED = "Údaje boli úspešne uložené.";
    private final String CUSTOMER_REMOVED = "Zákazník bol úspešne odstránený.";

    /***
     * Method to initialize the controller and write the message to the label
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String write = DATE_SAVED;
        if(Warehouse.getInstance().getController("deleteCustomer") != null){
            write = CUSTOMER_REMOVED;
        }
        message.setText(write);
    }

    /***
     * Method to go back to the main reservations
     * @throws IOException if the file is not found
     */
    public void backToMainReservations() throws IOException {
        if(Warehouse.getInstance().getController("deleteCustomer") != null) {
            Warehouse.getInstance().removeController("deleteCustomer");
        }
        Warehouse.getInstance().changeScene("CustomerManagement/customerManagementMain.fxml");
    }

    /***
     * Method to go back to the form that creates new customer
     * @throws IOException if the file is not found
     */
    public void backToForm() throws IOException {
        if(Warehouse.getInstance().getController("deleteCustomer") != null){
            Warehouse.getInstance().removeController("deleteCustomer");
            Warehouse.getInstance().changeScene("CustomerManagement/customerManagementMain.fxml");
            return;
        }
        Warehouse.getInstance().changeScene("CustomerManagement/CreateNewCustomer.fxml");
    }
}
