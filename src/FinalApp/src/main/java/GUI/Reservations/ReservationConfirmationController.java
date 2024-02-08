package GUI.Reservations;

import app.Warehouse;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

public class ReservationConfirmationController implements Initializable {
    @FXML
    private Label messageTop;
    @FXML
    private Label messageBottom;
    private final String DELETED_RECORD = "Záznam rezervácie bol odstránený.";
    private final String CANNOT_REMOVE = "Nie všetky pozície sú voľné.\nOdstránte tovar z:";
    private final String CREATE_CUSTOMER = "Rezervácia bola úspešne vytvorená.";

    /***
     * Inicializacia controllera
     * @param url
     * @param resourceBundle
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(Warehouse.getInstance().getController("cannotRemove") != null &&
                !((Set<String>) Warehouse.getInstance().getController("cannotRemove")).isEmpty()){
            String postionToFree = "";
            int index = 0;
            for(String positionName : (Set<String>) Warehouse.getInstance().getController("cannotRemove")){
                postionToFree += positionName + "\n";
                index++;
                if(index>=5) {break;}
            }
            if(index < ((Set<String>) Warehouse.getInstance().getController("cannotRemove")).size()){
                postionToFree += "a ďaľšie";
            }
            messageTop.setText(CANNOT_REMOVE);
            messageBottom.setText(postionToFree);
            return;
        }
        if(Warehouse.getInstance().getController("DeletedReservation") != null){
            messageBottom.setText(DELETED_RECORD);
            return;
        }
        messageBottom.setText(CREATE_CUSTOMER);
    }
    private boolean removeController(String name){
        if(Warehouse.getInstance().getController(name) != null){
            Warehouse.getInstance().removeController(name);
            return true;
        }
        return false;
    }

    public void backToForm() throws IOException {
        if(removeController("DeletedReservation") || removeController("cannotRemove")){
            Warehouse.getInstance().changeScene("Reservations/reservationsView.fxml");
            return;
        }
        Warehouse.getInstance().changeScene("Reservations/addReservationFirstForm.fxml");
    }

    public void backToMainReservations() throws IOException {
        removeController("cannotRemove");
        removeController("DeletedReservation");

        Warehouse.getInstance().changeScene("Reservations/reservationsMain.fxml");
    }


}
