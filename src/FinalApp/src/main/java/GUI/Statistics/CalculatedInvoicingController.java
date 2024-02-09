package GUI.Statistics;

import app.FileExporter;
import app.Warehouse;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class CalculatedInvoicingController implements javafx.fxml.Initializable{
    @FXML
    private Label calculatedPrice;
    @FXML
    private Label intervalFrom;
    @FXML
    private Label intervalTo;
    @FXML
    private Label customerName;
    /***
     * This variable is used to store the total number of reservations
     */
    int totalReservations = 0;

    /***
     * This method is used to calculate the invoicing price and set the labels.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        PriceForInvoicingController cont = (PriceForInvoicingController) Warehouse.
                getInstance().getController("priceForInvoicing");
        int price = cont.getPrice();
        StatisticMainPageController input = (StatisticMainPageController) Warehouse.getInstance().getController("statisticsMainPage");
        Date dateFrom = input.dateFromValue;
        Date dateTo = input.dateToValue;
        String customer = input.customer.getValue();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String dateFromSK = dateFormat.format(dateFrom);
        String dateToSK = dateFormat.format(dateTo);

        customerName.setText(customer);
        intervalFrom.setText(dateFromSK);
        intervalTo.setText(dateToSK);
        var dbh = Warehouse.getInstance().getDatabaseHandler();
        for (LocalDate date = dateFrom.toLocalDate();
            date.isBefore(dateTo.toLocalDate()) || date.isEqual(dateTo.toLocalDate());
            date = date.plusDays(1)) {
            int days = dbh.getNumberOfReservations(customer, Date.valueOf(date));
            totalReservations += days;
        }

        calculatedPrice.setText(totalReservations *price + " €");

    }

    /***
     * This method is used to go back to statistics main page
     * @throws IOException if the file is not found
     */
    public void backToStatistics() throws IOException {
        Warehouse.getInstance().changeScene("Statistics/statisticsMainPageForm.fxml");
    }
    /***
     * This method is used to go back to main menu
     * @throws IOException if the file is not found
     */
    public void backToMenu() throws IOException {
        Warehouse.getInstance().changeScene("mainMenu.fxml");
    }
    /***
     * This method is used to save the calculated invoicing price
     */
    public void saveInvoicingPrice() {
        FileExporter fe = new FileExporter();

        fe.exportInvoicingPDF(customerName.getText(), intervalFrom.getText(),
                intervalTo.getText(), calculatedPrice.getText(), totalReservations);
    }
}
