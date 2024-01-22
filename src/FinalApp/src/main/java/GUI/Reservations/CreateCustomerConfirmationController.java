package GUI.Reservations;

import app.Warehouse;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CreateCustomerConfirmationController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void backToMainReservations() throws IOException {
        Warehouse.getInstance().changeScene("Reservations/reservationsMain.fxml");
    }

    public void backToForm() throws IOException {
        Warehouse.getInstance().changeScene("Reservations/CreateNewCustomer.fxml");
    }
}
