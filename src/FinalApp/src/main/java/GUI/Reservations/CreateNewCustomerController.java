package GUI.Reservations;

import Entity.Customer;
import app.CustomersHandler;
import app.Warehouse;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.hibernate.exception.ConstraintViolationException;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CreateNewCustomerController implements Initializable {
    @FXML
    public TextField customerNameT; String customerName;
    @FXML
    public TextField addressT; String address;
    @FXML
    public TextField cityT; String city;
    @FXML
    public TextField postCodeT; String postCode;
    @FXML
    public TextField ICOT; String ICO;
    @FXML
    public TextField DICT; String DIC;
    @FXML
    public Label errorMessage;
    CustomersHandler customersHandler;
    final String EMPTY_NAME = "Zadajte meno zákazníka";
    final String USED_NAME = "Zadané meno existuje";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customersHandler = new CustomersHandler();
    }
    public void backToMainReservations() throws IOException {
        Warehouse.getInstance().changeScene("Reservations/reservationsMain.fxml");
    }



    public void addNewCustomer() throws IOException {
        customerName = String.valueOf(customerNameT.getText());
        address = String.valueOf(addressT.getText());
        city = String.valueOf(cityT.getText());
        postCode = String.valueOf(postCodeT.getText());
        ICO = String.valueOf(ICOT.getText());
        DIC = String.valueOf(DICT.getText());
        if(customerName.length() == 0 || customerName.replaceAll(" ", "").length() == 0){
            errorMessage.setText(EMPTY_NAME);
            return;
        }

        if(! customersHandler.checkCustomerName(customerName)){
            errorMessage.setText(USED_NAME);
            return;
        }

        if(address.length() == 0){
            errorMessage.setText("Zadajte adresu.");
            return;
        }

        if(city.length() == 0){
            errorMessage.setText("Zadajte mesto.");
            return;
        }
        if(postCode.length() == 0){
            errorMessage.setText("Zadajte PSČ.");
            return;
        }
        Customer newCustomer = new Customer(customerName, address, city, postCode, ICO, DIC);
        if(customersHandler.saveCustomer(newCustomer)) {
            Warehouse.getInstance().changeScene("Reservations/createCustomerConfirmation.fxml");
        }

        errorMessage.setText("Údaje sa nepodarilo uložiť");


    }
}
