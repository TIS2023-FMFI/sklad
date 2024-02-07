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
    private DatePicker dateFrom;
    @FXML
    private Date dateFromValue;
    @FXML
    private  DatePicker dateTo;
    @FXML
    private Date dateToValue;
    @FXML
    private Label customerName;
    @FXML
    private Label errorMessage;
    private Warehouse warehouse;
    private static final String STYLE = "-fx-font: 17px 'Calibri';";
    private static final String PAST_PICKED_DATE = "Dátum od nemôže byť starší ako dnešný dátum.";

    /***
     * Method to initialize the first form of adding reservation
     * @param url
     * @param resourceBundle
     */
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

    /***
     * Method to go back to main reservations
     * @throws IOException
     */
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
        else if(dateFromValue.toLocalDate().isBefore(LocalDate.now())){
            errorMessage.setText(PAST_PICKED_DATE);
            return false;
        }
       return true;
    }

    /***
     * Method to go to the next form of adding reservation
     * @throws IOException if the form is not found
     */
    public void nextForm() throws IOException {
        if(checkInputs()) {
            Warehouse.getInstance().changeScene("Reservations/addReservationSecondForm.fxml");
        }
    }

    /***
     * Method to save the date from the date picker
     * @param actionEvent
     */
    public void saveDateFrom(ActionEvent actionEvent) {
        LocalDate localDate = dateFrom.getValue();
        dateFromValue = Date.valueOf(localDate);
        if(warehouse.getController("dateFrom") != null){
            warehouse.removeController("dateFrom");
        }
        warehouse.addController("dateFrom", dateFromValue);

    }

    /***
     * Method to save the date from the date picker
     * @param actionEvent
     */
    public void saveDateTo(ActionEvent actionEvent) {
        LocalDate localDate = dateTo.getValue();
        dateToValue = Date.valueOf(localDate);
        if(warehouse.getController("dateTo") != null){
            warehouse.removeController("dateTo");
        }
        warehouse.addController("dateTo", dateToValue);
    }

    public class OwnDatePickerConverter extends StringConverter<LocalDate> {
        final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        /***
         * Method to convert the date to string
         * @param date LocalDate
         * @return if the date is not null, it returns the date in the format of dateFormatter, else it returns an empty string
         */
        @Override
        public String toString(LocalDate date) {
            if (date != null) {
                return dateFormatter.format(date);
            } else {
                return "";
            }
        }

        /***
         * Method to convert the string to date
         * @param string
         * @return
         */
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
