package GUI.Reservations;

import app.Reservation;
import app.Warehouse;
import jakarta.persistence.Convert;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class AddReservationSecondController implements Initializable {
    @FXML
    Label numberOfAllFreePositions;
    @FXML
    Label numberOfAllTallPositions;
    @FXML
    Label numberOfFreePositions;
    @FXML
    Label numberOfTallPositions;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Warehouse warehouse = Warehouse.getInstance();
        Reservation reservation = new Reservation();
        warehouse.addController("addReservationSecond", this);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        Date dateFrom =(Date) warehouse.getController("dateFrom");
        Date dateTo = (Date) warehouse.getController("dateTo");

        ChoiceBox<String> nameController = (ChoiceBox<String>)warehouse.getController("customerReservationName");
        numberOfAllFreePositions.setText(String.valueOf(reservation.getAllFreePositions(dateFrom, dateTo)));

        String name = nameController.getValue();
    }
    public void backToFirstForm() throws IOException {
        Warehouse.getInstance().changeScene("Reservations/addReservationFirstForm.fxml");
    }

}
