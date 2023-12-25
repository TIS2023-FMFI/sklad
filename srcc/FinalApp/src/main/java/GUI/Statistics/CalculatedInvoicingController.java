package GUI.Statistics;
import app.Warehouse;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class CalculatedInvoicingController implements javafx.fxml.Initializable{
    @FXML
    public Label calculatedPrice;
    @FXML
    public Label intervalFrom;
    @FXML
    public Label intervalTo;
    @FXML
    public Label customerName;

    /***
     * This method is used to calculate the invoicing price and set the labels.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        PriceForInvoicingController cont = (PriceForInvoicingController) Warehouse.
                getInstance().getController("priceForInvoicing");
        int price = cont.getPrice();
        StatisticMainPageContoller input = (StatisticMainPageContoller) Warehouse.getInstance().getController("statisticsMainPage");
        Date dateFrom = input.dateFromValue;
        Date dateTo = input.dateToValue;
        String customer = input.customer.getValue();

        customerName.setText(customer);
        intervalFrom.setText(dateFrom.toString());
        intervalTo.setText(dateTo.toString());
        var dbh = Warehouse.getInstance().getDatabaseHandler();
        int finalPrice = 0;
        for (LocalDate date = dateFrom.toLocalDate();
             date.isBefore(dateTo.toLocalDate()) || date.isEqual(dateTo.toLocalDate());
             date = date.plusDays(1)) {
            finalPrice += dbh.getNumberOfReservations(customer, Date.valueOf(date))*price;
        }

        calculatedPrice.setText(finalPrice + " €");

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
    public void saveInvoicingPrice(){
    }
}
