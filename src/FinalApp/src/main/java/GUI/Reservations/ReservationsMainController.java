package GUI.Reservations;

import GUI.StoreInProduct.HistoryRecord;
import app.Warehouse;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ReservationsMainController implements Initializable {
    @FXML
    public ChoiceBox<String> customer;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> customers = Warehouse.getInstance().getDatabaseHandler().getCustomersNames();
        customer.setItems(customers);
        if(customers.size() > 0) {
            customer.setValue(customers.get(0));
        }
        Warehouse.getInstance().addController("reservationMain", this);
    }
    public void backToMenu() throws IOException {
        Warehouse.getInstance().changeScene("mainMenu.fxml");
    }

    public void saveCustomerName(){
        Warehouse warehouse = Warehouse.getInstance();
        if(warehouse.getController("customerReservationName") != null){
            warehouse.removeController("customerReservationName");
        }
        warehouse.addController("customerReservationName", customer);
    }

    public void createNewCustomerScene() throws IOException {
        Warehouse.getInstance().changeScene("Reservations/createNewCustomer.fxml");
    }

    public void showReservationRecords() throws IOException {
        saveCustomerName();
        Warehouse.getInstance().changeScene("Reservations/reservationsView.fxml");
    }

    public void addNewReservation() throws IOException {
        Warehouse.getInstance().changeScene("Reservations/addReservationFirstForm.fxml");
    }
}
