package GUI.Statistics;

import app.Warehouse;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class StatisticMainPageContoller implements Initializable {
    @FXML
    public Label invoicingLabel;
    @FXML
    public ChoiceBox customers;
    @FXML
    DatePicker dateFrom;

    Date dateFromValue;
    @FXML
    DatePicker dateTo;

    Date dateToValue;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Warehouse.getInstance().addController("statisticsMainPage", this);
    }

    public void backToMenu() throws IOException {
        Warehouse.getInstance().changeScene("mainMenu.fxml");
    }
    public void showGraph() throws IOException {
        if (dateFromValue == null || dateToValue == null) {
            invoicingLabel.setText("Zlý dátum");
        }else if (dateFromValue.after(dateToValue)) {
            invoicingLabel.setText("Dátum od je väčší ako dátum do");
        }else {
            Warehouse.getInstance().changeScene("Statistics/graphForm.fxml");
        }
    }

    public void showInventoryCheck() throws IOException {
        Warehouse.getInstance().changeScene("Statistics/inventoryListForm.fxml");
    }

    /***
     * This method checks if current user is admin and if he is, it lets him begin the invoicing process.
     * @throws IOException if the file is not found
     */
    public void askForPriceForInvoicing() throws IOException {
        if (Warehouse.getInstance().getCurrentUser().getAdmin()) {
            Warehouse.getInstance().changeScene("Statistics/priceForInvoicingForm.fxml");
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
