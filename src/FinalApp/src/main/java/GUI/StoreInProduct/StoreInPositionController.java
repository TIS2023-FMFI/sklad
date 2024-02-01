package GUI.StoreInProduct;

import app.Warehouse;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class StoreInPositionController implements Initializable {
    @FXML
    private ChoiceBox<String> position;
    @FXML
    private TextField note;

    @FXML
    private Label errorMessage;

    @FXML
    private Button storeInProductButton;

    @FXML
    private Button continueStoringInButton;

    @FXML
    private Button saveRecordButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Warehouse.getStage().setOnCloseRequest(Event::consume);
        List<String> positions = Warehouse.getInstance().getStoreInInstance().getFreePositionsNames();

        if (positions.isEmpty()){
            errorMessage.setText("Nenašli sa žiadne pozície, vyhovujúce kritériam");
            setButtonsVisibility(false, false, true);
        }
        else {
            position.setItems(FXCollections.observableArrayList(positions));
            position.setValue(positions.get(0));
            setButtonsVisibility(true, true, false);
        }
        Warehouse.getInstance().addController("storeInPosition", this);
    }

    public void setButtonsVisibility(boolean visibilityStoreInButton, boolean visibilityContinueStoringButton,
                                     boolean visibilitySaveRecordButton){
        storeInProductButton.setVisible(visibilityStoreInButton);
        continueStoringInButton.setVisible(visibilityContinueStoringButton);
        saveRecordButton.setVisible(visibilitySaveRecordButton);
    }

    public void backToPalletInformationForm()throws IOException{
        Warehouse.getStage().setOnCloseRequest(null);
        Warehouse.getInstance().changeScene("StoreInProduct/PalletInformationForm.fxml");
    }

    public void storeInProduct() throws IOException {
        Warehouse.getStage().setOnCloseRequest(null);
        Warehouse warehouse = Warehouse.getInstance();

        warehouse.getStoreInInstance().storeInProduct();
        warehouse.getStoreInInstance().getHistoryRecord().addPallet();
        warehouse.getStoreInInstance().saveHistoryRecord();

        removeControllersAndInstances();

        warehouse.changeScene("mainMenu.fxml");
    }
    public void continueStoringIn() throws IOException{
        Warehouse.getStage().setOnCloseRequest(null);
        Warehouse warehouse = Warehouse.getInstance();

        warehouse.getStoreInInstance().storeInProduct();
        warehouse.getStoreInInstance().getHistoryRecord().addPallet();

        warehouse.removeController("palletInformation");
        warehouse.removeController("storeInPosition");
        warehouse.getStoreInInstance().removePalletInformationDataSet();

        warehouse.changeScene("StoreInProduct/palletInformationForm.fxml");
    }

    public void saveRecord() throws IOException{
        Warehouse.getStage().setOnCloseRequest(null);
        Warehouse warehouse = Warehouse.getInstance();

        warehouse.getStoreInInstance().saveHistoryRecord();
        removeControllersAndInstances();

        warehouse.changeScene("mainMenu.fxml");
    }

    public void removeControllersAndInstances(){
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.removeController("customerTruckNumber");
        warehouse.removeController("palletInformation");
        warehouse.removeController("storeInPosition");

        warehouse.deleteStoreInProductInstance();
    }

    public String getPosition() {
        return position.getValue();
    }

    public String getNote() {
        return note.getText();
    }
}
