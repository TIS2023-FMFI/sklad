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
    private static final String MORE_THAN_ZERO = "Musíte vybrať viac miest ako 0";
    private static final String MORE_THAN_AVAIABLE = "Nemožno rezervovať viac miest ako ";
    private static final String MORE_THAN_TALL_AVIABLE = "Nemožno rezervovať viac vysokých miest ako ";
    private static final String MORE_TALL_THAN_LOW = "Počet vysokých miest nesmie byť väčší ako celkový počet miest.";
    private static final String NUMBER_CONTAINS_CHAR = "Číslo obsahuje nepovolené znaky";
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
            errorMessage.setText(NUMBER_CONTAINS_CHAR);
            return;
        }
        if(getPosition == 0){
            errorMessage.setText(MORE_THAN_ZERO);
            return;
        }
        if(getPosition > allFreePosition){
            errorMessage.setText(MORE_THAN_AVAIABLE + allFreePosition);
            return;
        }

        if(getTall > allTallPosition){
            errorMessage.setText(MORE_THAN_TALL_AVIABLE + allTallPosition);
            return;
        }

        if(getTall > getPosition){
            errorMessage.setText(MORE_TALL_THAN_LOW);
            return;
        }

        int getLow = getPosition - getTall;
        if(getLow > allFreePosition - allTallPosition){
            int difference = (getLow - (allFreePosition - allTallPosition));
            getTall += difference;
            getLow -= difference;
        }

        String nameCustomer = ((ChoiceBox<String>)warehouse.getController("customerReservationName")).getValue();
        if(warehouse.getController("tallPositions") != null){
            warehouse.removeController("tallPositions");
        }
        if(warehouse.getController("lowPositions") != null){
            warehouse.removeController("lowPositions");
        }

        warehouse.addController("tallPositions", getTall);
        warehouse.addController("lowPositions", getLow);

        reservation.findPotisionsToReserve(getLow, getTall, nameCustomer);
        Warehouse.getInstance().changeScene("Reservations/warehouseLayoutRowsReservationForm.fxml");
    }


}
