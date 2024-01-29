package GUI.Statistics;

import app.Warehouse;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PriceForInvoicingController implements Initializable{
    @FXML
    private TextField priceForOnePositionForOneDay;
    /***
     * This variable is used to store the price for one position for one day.
     */
    private Integer price;
    @FXML
    private Label warningLabel;

    /***
     * This method is called when the page is opened. It adds this controller to the list of controllers.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Warehouse.getInstance().addController("priceForInvoicing", this);
    }

    /***
     * This method checks if the input is a number and if it is, it saves it to the variable price.
     * @return true if the input is a number, false if it is not.
     */
    public boolean savePriceForInvoicing(){
        try {
            price = Integer.parseInt(priceForOnePositionForOneDay.getText());
        }catch (NumberFormatException e){
            warningLabel.setText("Zadaná hodnota nie je číslo");
            return false;
        }
        return true;
    }

    /***
     * This method is used to go back to statistics main page
     * @throws IOException if the file is not found
     */
    public void backToStatistics() throws IOException {
        Warehouse.getInstance().changeScene("Statistics/statisticsMainPageForm.fxml");
    }

    /***
     * This method is used to go to the page that calculates invoicing.
     * @throws IOException if the file is not found
     */
    public void calculateInvoicing() throws IOException {
        if (priceForOnePositionForOneDay.getText().isEmpty()) {
            warningLabel.setText("Zadajte hodnotu");
        }else{
            if (savePriceForInvoicing()) {
                Warehouse.getInstance().changeScene("Statistics/calculatedInvoicingForm.fxml");
            }
        }
    }

    public Integer getPrice(){
        return price;
    }
}
