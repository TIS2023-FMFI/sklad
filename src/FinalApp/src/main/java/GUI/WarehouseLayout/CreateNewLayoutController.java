package GUI.WarehouseLayout;

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
import java.util.ResourceBundle;

public class CreateNewLayoutController implements Initializable {

    private FileChooser fileChooser;
    @FXML
    private Label errorMessage;
    @FXML
    private Label path;
    private File fileToLoad;
    private static final String CHOOSE_FILE = "Nebol vybraný súbor.";
    private static final String FORMAT_EXCEPTION = "Vybraný súbor má zlú štruktúru.\n";

    /***
     * Initializes the controller and sets title
     * @param url The location used to resolve relative paths for the root object, or null.
     * @param resourceBundle The resources used to localize the root object, or null.
     */
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

    /**
     * Saves new positions by loading data from a file and adding them to the warehouse.
     * If the file is incorrect or the data format is wrong, it displays an error message.
     *
     * @throws IOException
     */
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

    /***
     * Changes scene to main menu
     * @throws IOException
     */
    @FXML
    public void backToForm() throws IOException {
        Warehouse.getInstance().changeScene("Reservations/reservationsMain.fxml");
    }
}
