package GUI.Reservations;

import app.Warehouse;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddReservationFirstController implements Initializable {
    @FXML
    DatePicker dateFrom;
    Date dateFromValue;
    @FXML
    DatePicker dateTo;
    Date dateToValue;
    @FXML
    Label customerName;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       Warehouse warehouse = Warehouse.getInstance();
       warehouse.addController("addReservation", this);
        ChoiceBox<String> nameController = (ChoiceBox<String>)warehouse.getController("customerReservationName");
        String name = nameController.getValue();
        customerName.setText(name);
    }

    public void backToMainReservations() throws IOException {
        Warehouse.getInstance().changeScene("Reservations/reservationsMain.fxml");
    }

    public void nextForm() throws IOException {
        Warehouse.getInstance().changeScene("Reservations/addReservationSecondForm.fxml");
    }
    public void saveDateFrom(ActionEvent actionEvent) {
        LocalDate localDate = dateFrom.getValue();
        dateFromValue = Date.valueOf(localDate);
    }

    public void saveDateTo(ActionEvent actionEvent) {
        LocalDate localDate = dateTo.getValue();
        dateToValue = Date.valueOf(localDate);
    }
}
