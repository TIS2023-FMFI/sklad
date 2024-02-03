package GUI.WarehouseLayout;

import Entity.Customer;
import Exceptions.FileNotFound;
import Exceptions.WrongStringFormat;
import app.LoadPositions;
import app.Reservation;
import app.Warehouse;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class CreateNewLayoutController implements Initializable {

    private static final String STYLE = "-fx-font: 17px 'Calibri'; -fx-alignment: CENTER;";
    public FileChooser fileChooser;
    @FXML
    Label errorMessage;
    @FXML
    Label path;
    private File fileToLoad;
    private final String CHOOSE_FILE = "Nebol vybraný súbor.";
    private final String FORMAT_EXCEPTION = "Vybraný súbor má zlú štruktúru.\n";


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileChooser = new FileChooser();
        fileChooser.setTitle("Zvoľte súbor na nahranie");
        fileToLoad = null;

    }
    @FXML
    protected void selectFile(){
        fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Txt files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        fileToLoad = fileChooser.showOpenDialog(Warehouse.getStage());

        if(fileToLoad != null) {
            path.setText(fileToLoad.getAbsolutePath());
        }
    }

    @FXML
    public void saveNewPositions() throws IOException {
        if(!isFileCorrect()){
            return;
        }
        try {
            LoadPositions load = new LoadPositions(fileToLoad.getAbsolutePath());
            load.addPositions();
            Warehouse.getInstance().addController("newPositions", load.getFinalPositions());
            Warehouse.getInstance().changeScene("WarehouseLayout/layoutConfirmation.fxml");

        } catch (FileNotFound | WrongStringFormat e) {
            errorMessage.setText(FORMAT_EXCEPTION + e.getMessage());
        }
    }

    private boolean isFileCorrect(){
        if(fileToLoad == null){
            errorMessage.setText(CHOOSE_FILE);
            return false;
        }

        return true;
    }

    @FXML
    public void backToForm() throws IOException {
        Warehouse.getInstance().changeScene("Reservations/reservationsMain.fxml");
    }
}
