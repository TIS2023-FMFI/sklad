package GUI.StoreInProduct;

import app.Warehouse;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CustomerTruckNumberController implements Initializable {
    @FXML
    private ChoiceBox<String> customer;
    @FXML
    private Spinner<Integer> truckNumber;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> customers = Warehouse.getInstance().getDatabaseHandler().getCustomersNames();
        customer.setItems(customers);
        customer.setValue(customers.get(0));

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5);
        valueFactory.setValue(1);
        truckNumber.setValueFactory(valueFactory);

        Warehouse.getInstance().addController("customerTruckNumber", this);
    }

    public void nextToInformationForm() throws IOException{
        Warehouse.getInstance().changeScene("StoreInProduct/palletInformationForm.fxml");
    }

    public void backToMenu() throws IOException {
        Warehouse.getInstance().changeScene("mainMenu.fxml");
    }

    public String getCustomer() {
        return customer.getValue();
    }

    public Integer getTruckNumber() {
        return truckNumber.getValue();
    }
}
