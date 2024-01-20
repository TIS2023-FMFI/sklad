package GUI.Reservations;

import app.Warehouse;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
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
        warehouse.addController("addReservationSecond", this);
        ChoiceBox<String> nameController = (ChoiceBox<String>)warehouse.getController("customerReservationName");
        String name = nameController.getValue();
    }
    public void backToFirstForm() throws IOException {
        Warehouse.getInstance().changeScene("Reservations/addReservationFirstForm.fxml");
    }

}
