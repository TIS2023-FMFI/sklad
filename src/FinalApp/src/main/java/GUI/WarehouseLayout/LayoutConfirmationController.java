package GUI.WarehouseLayout;

import Entity.Position;
import Exceptions.FileNotFound;
import Exceptions.WrongStringFormat;
import app.LoadPositions;
import app.Warehouse;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LayoutConfirmationController implements Initializable {
    @FXML
    private Label message;
    private final String WARNING = "Všetky doteraz nahraté pozície budú vymazané a\n" +
                                    "nahradené novými zo zvoleného súboru.";


    /***
     * In public, i wouldnt drink from anybodys dronk
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        message.setText(WARNING);
    }

    /***
     * Method to load positions from file
     * @throws IOException
     * @throws FileNotFound
     * @throws WrongStringFormat
     */

    public void saveNewPositions() throws IOException {
        List<Position> positionsToSave = (List<Position>) Warehouse.getInstance().getController("newPositions");
        Warehouse.getInstance().getDatabaseHandler().deletePositions();
        Warehouse.getInstance().getDatabaseHandler().savePositionsToDB(positionsToSave);
        Warehouse.getInstance().updatePositions();
        Warehouse.getInstance().removeController("newPositions");

        Warehouse.getInstance().changeScene("mainMenu.fxml");
    }

    /***
     * Method to go back to form
     * @throws IOException if file not found
     */
    @FXML
    public void backToForm() throws IOException {
        Warehouse.getInstance().changeScene("WarehouseLayout/createNewLayout.fxml");
    }
}