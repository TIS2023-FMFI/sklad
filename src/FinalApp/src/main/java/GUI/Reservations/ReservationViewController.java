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

    private static final String STYLE = "-fx-font: 17px 'Calibri'; -fx-alignment: CENTER;";

    private ObservableList<Map<String, Object>> items = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Reservation reservation = new Reservation();

        TableColumn<Map, String> dateFromColumn = new TableColumn<>("Od");
        dateFromColumn.setCellValueFactory(new MapValueFactory<>("from"));
        dateFromColumn.setStyle(STYLE);
        dateFromColumn.setPrefWidth(168);

        TableColumn<Map, String> dateUntilColumn = new TableColumn<>("Do");
        dateUntilColumn.setCellValueFactory(new MapValueFactory<>("until"));
        dateUntilColumn.setStyle(STYLE);
        dateUntilColumn.setPrefWidth(138);

        TableColumn<Map, Integer> numberOfPositionColumn = new TableColumn<>("Počet pozícií");
        numberOfPositionColumn.setCellValueFactory(new MapValueFactory<>("numberOfReservedPositions"));
        numberOfPositionColumn.setStyle(STYLE);
        numberOfPositionColumn.setPrefWidth(118);

        TableColumn<Map, Void> editButton = new TableColumn<>("");
        editButton.setCellValueFactory(new MapValueFactory<>("edit"));
        editButton.setStyle(STYLE);
        editButton.setPrefWidth(118);

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