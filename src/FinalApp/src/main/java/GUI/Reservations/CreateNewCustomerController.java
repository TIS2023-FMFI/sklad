package GUI.Reservations;

import app.CustomersHandler;
import app.Warehouse;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CreateNewCustomerController implements Initializable {
    @FXML
    public TextField customerName;
    @FXML
    public Label errorMessage;
    CustomersHandler customersHandler;
    final String EMPTY_NAME = "Zadajte meno zákazníka";
    final String USED_NAME = "Zadané meno existuje";
    final String WRONG_FORMAT = "Meno nemôže obsahovať medzery";
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Warehouse.getInstance().addController("createNewCustomer", this);
        customersHandler = new CustomersHandler();
    }
    public void backToMainReservations() throws IOException {
        Warehouse.getInstance().changeScene("Reservations/reservationsMain.fxml");
    }



    public void addNewCustomer(){
        String name = String.valueOf(customerName.getText());
        if(name.length() == 0){
            errorMessage.setText(EMPTY_NAME);
        }
        else if(name.contains(" ")){
            errorMessage.setText(WRONG_FORMAT);
        }
        else if(!customersHandler.saveCustomer(name)){
            errorMessage.setText(USED_NAME);
        }
    }
}
