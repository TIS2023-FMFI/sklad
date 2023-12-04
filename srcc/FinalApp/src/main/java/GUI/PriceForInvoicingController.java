package GUI;

import app.Warehouse;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class PriceForInvoicingController {
    @FXML
    public TextField priceForOnePositionForOneDay;
    Integer price;
    @FXML
    public Label warningLabel;

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
        Warehouse.getInstance().changeScene("statisticsMainPage.fxml");
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
                Warehouse.getInstance().changeScene("calculatedInvoicing.fxml");
            }
        }
    }
}
