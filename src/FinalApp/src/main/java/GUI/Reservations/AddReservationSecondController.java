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

    Reservation reservation;
    Warehouse warehouse;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        warehouse = Warehouse.getInstance();
        reservation = new Reservation();

        Date dateFrom =(Date) warehouse.getController("dateFrom");
        Date dateTo = (Date) warehouse.getController("dateTo");

        Pair<Integer, Integer> counter = reservation.countAllFreePositions(dateFrom, dateTo);
        allFreePosition = counter.getKey();
        allTallPosition = counter.getValue();
        numberOfAllFreePositions.setText(String.valueOf(counter.getKey()));
        numberOfAllTallPositions.setText(String.valueOf(counter.getValue()));
        if(warehouse.getController("positionsToSave") != null){
            warehouse.removeController("positionsToSave");
        }
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
            errorMessage.setText("Musíte vybrať viac miest ako 0");
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

        int getLow = getPosition - getTall;
        if(getLow > allFreePosition - allTallPosition){
            int difference = (getLow - (allFreePosition - allTallPosition));
            getTall += difference;
            getLow -= difference;
        }

        String nameCustomer = ((ChoiceBox<String>)warehouse.getController("customerReservationName")).getValue();
        if(warehouse.getController("numberOfPosition") != null){
            warehouse.removeController("numberOfPosition");
        }

        warehouse.addController("numberOfPosition", getLow + getTall);

        reservation.findPotisionsToReserve(getLow, getTall, nameCustomer);
        Warehouse.getInstance().changeScene("Reservations/warehouseLayoutRowsReservationForm.fxml");
    }


}
