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

    /***
     * Initialize the customer and truck number values
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CustomerTruckNumberDataSet dataSet = Warehouse.getInstance().getStoreInInstance().getCustomerTruckNumberDataSet();
        if (dataSet == null){
            setupDefaultvalues();
        }
        else {
            setupValuesFromDataSet(dataSet);
        }
        Warehouse.getInstance().addController("customerTruckNumber", this);
    }

    private void setupDefaultvalues(){
        ObservableList<String> customers = Warehouse.getInstance().getDatabaseHandler().getCustomersNames();
        customers.remove(Warehouse.getInstance().getDatabaseHandler().getRootCustomer().getName());
        customer.setItems(customers);
        customer.setValue(customers.get(0));

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5);
        valueFactory.setValue(1);
        truckNumber.setValueFactory(valueFactory);
    }

    private void setupValuesFromDataSet(CustomerTruckNumberDataSet dataSet) {
        ObservableList<String> customers = Warehouse.getInstance().getDatabaseHandler().getCustomersNames();
        customer.setItems(customers);
        customer.setValue(dataSet.customer());

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5);
        valueFactory.setValue(dataSet.truckNumber());
        truckNumber.setValueFactory(valueFactory);
    }

    /***
     * Go to the next form and check if the customer and truck number are valid
     * @throws IOException if the form is not found
     */
    public void nextToInformationForm() throws IOException{
        Warehouse.getInstance().getStoreInInstance().initializeCustomerTruckNumberDataSet(getCustomer(), getTruckNumber());
        HistoryRecord historyRecord = Warehouse.getInstance().getStoreInInstance().getHistoryRecord();
        historyRecord.setCustomerID(Warehouse.getInstance().getDatabaseHandler().getCustomer(customer.getValue()).getId());
        historyRecord.setTruckNumber(getTruckNumber());
        Warehouse.getInstance().changeScene("StoreInProduct/palletInformationForm.fxml");
    }

    /***
     * Go back to the main menu
     * @throws IOException if the form is not found
     */
    public void backToMenu() throws IOException {
        Warehouse.getInstance().removeController("customerTruckNumber");
        Warehouse.getInstance().getStoreInInstance().removeCustomerTruckDataSet();
        Warehouse.getInstance().changeScene("mainMenu.fxml");
    }

    public String getCustomer() {
        return customer.getValue();
    }

    public int getTruckNumber() {
        return truckNumber.getValue();
    }
}
