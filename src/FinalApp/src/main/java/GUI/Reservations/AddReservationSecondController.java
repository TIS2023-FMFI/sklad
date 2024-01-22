package GUI.Reservations;

import app.Reservation;
import app.Warehouse;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class AddReservationSecondController implements Initializable {
    @FXML
    Label numberOfAllFreePositions;
    @FXML
    Label numberOfAllTallPositions;
    @FXML
    Label errorMessage;
    @FXML
    TextField numberOfFreeField;
    @FXML
    TextField numberOfTallField;

    int allFreePosition;
    int allTallPosition;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Warehouse warehouse = Warehouse.getInstance();
        Reservation reservation = new Reservation();
        warehouse.addController("addReservationSecond", this);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date dateFrom =(Date) warehouse.getController("dateFrom");
        Date dateTo = (Date) warehouse.getController("dateTo");


        ChoiceBox<String> nameController = (ChoiceBox<String>)warehouse.getController("customerReservationName");

        Pair<Integer, Integer> counter = reservation.countAllFreePositions(dateFrom, dateTo);
        allFreePosition = counter.getKey();
        allTallPosition = counter.getValue();
        numberOfAllFreePositions.setText(String.valueOf(counter.getKey()));
        numberOfAllTallPositions.setText(String.valueOf(counter.getValue()));
    }
    public void backToFirstForm() throws IOException {
        Warehouse.getInstance().changeScene("Reservations/addReservationFirstForm.fxml");
    }

    public void findPositionsToSave() throws IOException{
        int getPosition = 0;
        int getTall = 0;
        try {
            getPosition = Integer.parseInt(numberOfFreeField.getText());
            getTall = Integer.parseInt(numberOfTallField.getText());
        }
        catch (NumberFormatException e){
            errorMessage.setText("Číslo obsahuje nepovolené znaky");
            return;
        }
        if(getPosition == 0){
            errorMessage.setText("Musíte vybrať vaic miest ako 0");
            return;
        }
        if(getPosition > allFreePosition){
            errorMessage.setText("Nemožno rezervovať viac miest ako " + allFreePosition);
            return;
        }

        if(getTall > allTallPosition){
            errorMessage.setText("Nemožno rezervovať viac vysokých miest ako " + allTallPosition);
            return;
        }

        if(getTall > getPosition){
            errorMessage.setText("Nemožno rezervovať viac vysokých miest ako obyčaných.");
            return;
        }

        Warehouse.getInstance().changeScene("Reservations/createReservationConfirmation.fxml");
    }

}
