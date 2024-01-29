package GUI.Reservations;

import Entity.Customer;
import app.Reservation;
import app.Warehouse;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;


import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class ReservationViewController implements Initializable {
    @FXML
    public TableView reservationTable;

    private ObservableList<Map<String, Object>> items = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Reservation reservation = new Reservation();
        TableColumn<Map, String> dateFromColumn = new TableColumn<>("Od");
        dateFromColumn.setCellValueFactory(new MapValueFactory<>("from"));

        TableColumn<Map, String> dateUntilColumn = new TableColumn<>("Do");
        dateUntilColumn.setCellValueFactory(new MapValueFactory<>("unitl"));

        TableColumn<Map, Integer> numberOfPositionColumn = new TableColumn<>("Počet pozícií");
        numberOfPositionColumn.setCellValueFactory(new MapValueFactory<>("numberOfReservedPositions"));

        TableColumn<Map, Void> editButton = new TableColumn<>("");
        editButton.setCellValueFactory(new MapValueFactory<>("edit"));

        reservationTable.getColumns().addAll(dateFromColumn, dateUntilColumn, numberOfPositionColumn, editButton);
        ChoiceBox<String> nameController = (ChoiceBox<String>)Warehouse.getInstance().getController("customerReservationName");
        String name = nameController.getValue();
        Customer customer = Warehouse.getInstance().getDatabaseHandler().getCustomer(name);
        items.addAll(reservation.setReservationTable(customer));
        reservationTable.getItems().addAll(items);
    }

    public void backToMainReservations() throws IOException {
        Warehouse.getInstance().changeScene("Reservations/reservationsMain.fxml");
    }
}