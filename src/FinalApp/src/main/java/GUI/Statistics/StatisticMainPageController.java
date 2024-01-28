package GUI.Statistics;

import Entity.Customer;
import app.Warehouse;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class StatisticMainPageController implements Initializable {
    @FXML
    public Label invoicingLabel;
    @FXML
    public ChoiceBox<String> customer;
    public Button invoicingButton;
    @FXML
    DatePicker dateFrom;

    Date dateFromValue;
    @FXML
    DatePicker dateTo;

    Date dateToValue;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> customers = Warehouse.getInstance().getDatabaseHandler().getCustomersNames();
        customer.setItems(customers);
        customer.setValue(customers.get(0));
        if (!Warehouse.getInstance().getCurrentUser().getAdmin()){
            invoicingButton.setVisible(false);
        }
        Warehouse.getInstance().addController("statisticsMainPage", this);
    }

    /***
     * This method is called when the user clicks on the "Back" button. It takes him back to the main menu.
     * @throws IOException if the file is not found
     */
    public void backToMenu() throws IOException {
        Warehouse.getInstance().changeScene("mainMenu.fxml");
    }

    private boolean checkInputs(){
        if (dateFromValue == null || dateToValue == null) {
            invoicingLabel.setText("Zlý dátum");
        }else if (dateFromValue.after(dateToValue)) {
            invoicingLabel.setText("Dátum od je väčší ako dátum do");
        }else if (customer.getValue() == null) {
            invoicingLabel.setText("Vyberte zákazníka");
        }else {
            return true;
        }
        return false;
    }

    /***
     * This method checks if all parameters are set and then shows the graph of all exports and imports for chosen dates..
     * @throws IOException if the file is not found
     */
    public void showGraph() throws IOException {
        if (checkInputs()) {
            Warehouse.getInstance().changeScene("Statistics/graphForm.fxml");
        }
    }

    /***
     * This method checks if current user is admin and if he is, it lets him begin the invoicing process.
     * @throws IOException if the file is not found
     */
    public void showInventoryCheck() throws IOException {
        Warehouse.getInstance().changeScene("Statistics/inventoryListForm.fxml");
    }

    /***
     * This method checks if current user is admin and if he is, it lets him begin the invoicing process.
     * @throws IOException if the file is not found
     */
    public void askForPriceForInvoicing() throws IOException {
        if (Warehouse.getInstance().getCurrentUser().getAdmin()) {
            if (checkInputs()) {
                Warehouse.getInstance().changeScene("Statistics/priceForInvoicingForm.fxml");
            }
        }else{
            invoicingLabel.setText("Pre prístup na túto stránku nemáte dostatočné oprávnenia");
        }
    }

    @FXML
    private void saveDateFrom() {
        LocalDate localDate = dateFrom.getValue();
        dateFromValue = Date.valueOf(localDate);
    }

    @FXML
    private void saveDateTo() {
        LocalDate localDate = dateTo.getValue();
        dateToValue = Date.valueOf(localDate);
    }


}
