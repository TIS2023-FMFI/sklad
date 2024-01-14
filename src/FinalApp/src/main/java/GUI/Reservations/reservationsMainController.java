package Reservations;

import app.Warehouse;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class reservationsMainController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Warehouse.getInstance().addController("reservationMain", this);
    }
}
