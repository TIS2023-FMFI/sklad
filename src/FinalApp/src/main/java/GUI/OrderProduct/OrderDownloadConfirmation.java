package GUI.OrderProduct;

import app.DatabaseHandler;
import app.FileExporter;
import app.Warehouse;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.time.LocalDate;

public class OrderDownloadConfirmation {

    public TextField numOfPallets;
    public Label errorLabel;

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

    public boolean isNumber(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
