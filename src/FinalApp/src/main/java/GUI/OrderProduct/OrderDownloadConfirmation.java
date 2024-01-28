package GUI.OrderProduct;

import app.DatabaseHandler;
import app.FileExporter;
import app.Warehouse;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.time.LocalDate;

public class OrderDownloadConfirmation {

    @FXML
    private TextField numOfPallets;
    @FXML
    private Label errorLabel;

    /***
     * Exports the order to a file and returns to the main menu
     * @throws IOException if the file cannot be created
     */
    public void backToMenu() throws IOException {
        if (numOfPallets.getText().equals("")) {
            errorLabel.setText("Zadajte počet palet");
            return;
        }
        if (!isNumber(numOfPallets.getText())) {
            errorLabel.setText("Počet paliet musí byť číslo");
            return;
        }
        OrderShowPositionsController cont = (OrderShowPositionsController) Warehouse.getInstance().
                getController("OrderShowPositionsController");
        DatabaseHandler db = Warehouse.getInstance().getDatabaseHandler();
        int truckNum = db.getTruckNum(cont.cust.getId(), LocalDate.now(), false);
        db.saveHistoryRecord(cont.cust.getId(), Integer.parseInt(numOfPallets.getText()), truckNum, false);
        Warehouse.getInstance().changeScene("mainMenu.fxml");
    }

    private boolean isNumber(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
