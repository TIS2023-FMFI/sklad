package GUI.StoreInProduct;

import app.StoreInProduct;
import app.Warehouse;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
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

    StoreInProduct storeInProduct;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        storeInProduct = new StoreInProduct();
        List<String> positions = storeInProduct.getFreePositionsNames();
        position.setItems(FXCollections.observableArrayList(positions));
        position.setValue(positions.get(0));
        Warehouse.getInstance().addController("storeInPosition", this);
    }
    public void storeInProduct() throws IOException {
        storeInProduct.storeInProduct();
        Warehouse.getInstance().changeScene("mainMenu.fxml");
    }
    public void continueStoringIn() throws IOException{
        storeInProduct();
        Warehouse.getInstance().changeScene("StoreInProduct/palletInformationForm.fxml");
    }

    public String getPosition() {
        return position.getValue();
    }

    public String getNote() {
        return note.getText();
    }
}
