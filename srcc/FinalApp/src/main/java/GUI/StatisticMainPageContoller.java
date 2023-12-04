package GUI;

import app.Warehouse;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

public class StatisticMainPageContoller {
    @FXML
    public Label invoicingLabel;
    @FXML
    DatePicker dateFrom;

    Date dateFromValue;
    @FXML
    DatePicker dateTo;

    Date dateToValue;
    @FXML
    String customersStats;

    public void backToMenu() throws IOException {
        Warehouse.getInstance().changeScene("mainMenu.fxml");
    }
    public void showGraph() throws IOException {
        Warehouse.getInstance().changeScene("graph.fxml");
    }

    public void showInventoryCheck() throws IOException {
        Warehouse.getInstance().changeScene("inventoryList.fxml");
    }

    /***
     * This method checks if current user is admin and if he is, it lets him begin the invoicing process.
     * @throws IOException if the file is not found
     */
    public void askForPriceForInvoicing() throws IOException {
        if (Warehouse.getInstance().getCurrentUser().getAdmin()) {
            Warehouse.getInstance().changeScene("priceForInvoicing.fxml");
        }else{
            invoicingLabel.setText("Pre prístup na túto stránku nemáte dostatočné oprávnenia");
        }
    }

    public void saveDateFrom(ActionEvent actionEvent) {
        LocalDate localDate = dateFrom.getValue();
        dateFromValue = Date.valueOf(localDate);
    }

    public void saveDateTo(ActionEvent actionEvent) {
        LocalDate localDate = dateTo.getValue();
        dateToValue = Date.valueOf(localDate);
    }
}
