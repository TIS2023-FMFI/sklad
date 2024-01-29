package GUI.Reservations;

import app.Warehouse;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    @FXML
    Label errorMessage;
    Warehouse warehouse;
    private final String STYLE = "-fx-font: 20px 'Calibri';";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       warehouse = Warehouse.getInstance();
        ChoiceBox<String> nameController = (ChoiceBox<String>)warehouse.getController("customerReservationName");
        String name = nameController.getValue();
        customerName.setText(name);
        dateFrom.setStyle(STYLE);
        dateTo.setStyle(STYLE);
        OwnDatePickerConverter converter = new OwnDatePickerConverter();

        dateFrom.setConverter(converter);
        dateTo.setConverter(converter);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if(warehouse.getController("dateFrom") != null){
            dateFrom.setValue(LocalDate.parse(warehouse.getController("dateFrom").toString(), formatter));
            dateFromValue = Date.valueOf(dateFrom.getValue());
        }
        if(warehouse.getController("dateTo") != null){
            dateTo.setValue(LocalDate.parse(warehouse.getController("dateTo").toString(), formatter));
            dateToValue = Date.valueOf(dateTo.getValue());
        }
    }

    public void backToMainReservations() throws IOException {
        Warehouse.getInstance().changeScene("Reservations/reservationsMain.fxml");
    }
    private boolean checkInputs(){
        if (dateFromValue == null || dateToValue == null) {
            errorMessage.setText("Prázdny dátum");
            return false;
        }else if (dateFromValue.after(dateToValue)) {
            errorMessage.setText("Dátum od je väčší ako dátum do");
            return false;
        }
       return true;
    }

    public void nextForm() throws IOException {
        if(checkInputs()) {
            Warehouse.getInstance().changeScene("Reservations/addReservationSecondForm.fxml");
        }
    }
    public void saveDateFrom(ActionEvent actionEvent) {
        LocalDate localDate = dateFrom.getValue();
        dateFromValue = Date.valueOf(localDate);
        if(warehouse.getController("dateFrom") != null){
            warehouse.removeController("dateFrom");
        }
        warehouse.addController("dateFrom", dateFromValue);

    }

    public void saveDateTo(ActionEvent actionEvent) {
        LocalDate localDate = dateTo.getValue();
        dateToValue = Date.valueOf(localDate);
        if(warehouse.getController("dateTo") != null){
            warehouse.removeController("dateTo");
        }
        warehouse.addController("dateTo", dateToValue);
    }

    public class OwnDatePickerConverter extends StringConverter<LocalDate> {
        private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        @Override
        public String toString(LocalDate date) {
            if (date != null) {
                return dateFormatter.format(date);
            } else {
                return "";
            }
        }
        @Override
        public LocalDate fromString(String string) {
            if (string != null && !string.isEmpty()) {
                return LocalDate.parse(string, dateFormatter);
            } else {
                return null;
            }
        }
    }
}
