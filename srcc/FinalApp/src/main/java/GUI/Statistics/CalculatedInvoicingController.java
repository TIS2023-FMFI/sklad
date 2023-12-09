package GUI.Statistics;
import app.Warehouse;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
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
