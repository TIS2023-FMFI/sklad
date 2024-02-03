package GUI.CustomerManagement;

import Entity.Customer;
import app.Warehouse;
import com.jfoenix.controls.JFXButton;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CustomerManagementMainController implements Initializable {
    @FXML
    public ChoiceBox<String> customer;
    @FXML
    public Label errorMessage;
    @FXML
    private Button companyInfo;
    @FXML
    Button delete;
    @FXML
    Button showCustomer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Customer us = Warehouse.getInstance().getDatabaseHandler().getCustomerById(43);
        if (us == null) {
            us = saveGefcoAsCustomer();
        }
        ObservableList<String> customers = Warehouse.getInstance().getDatabaseHandler().getCustomersNames();
        customers.remove(us.getName());

        customer.setItems(customers);
        customer.setStyle("-fx-font: 20px 'Calibri';");
        if(customers.size() > 0) {
            customer.setValue(customers.get(0));
        }
        else{
            delete.setDisable(true);
            showCustomer.setDisable(true);
        }
        if(Warehouse.getInstance().getController("customerName") != null){
            Warehouse.getInstance().removeController("customerName");
        }
        Warehouse.getInstance().addController("customerName", customer.getValue());
    }

    public void saveCustomerName(){
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.addController("customerName", customer.getValue());
    }
    public void showInformation() throws IOException {
        Warehouse warehouse = Warehouse.getInstance();
        if(warehouse.getController("customerName") != null){
            warehouse.removeController("customerName");
        }
        warehouse.addController("customerName", customer.getValue());
        Warehouse.getInstance().changeScene("CustomerManagement/createNewCustomer.fxml");
    }

    public void backToMainReservation() throws IOException {
        Warehouse.getInstance().changeScene("Reservations/reservationsMain.fxml");
    }
    public void createNewCustomerScene() throws IOException {
        if(Warehouse.getInstance().getController("customerName") != null){
            Warehouse.getInstance().removeController("customerName");
        }
        Warehouse.getInstance().changeScene("CustomerManagement/createNewCustomer.fxml");
    }

    public void deleteCustomerScene() throws IOException {
        Warehouse.getInstance().changeScene("CustomerManagement/deleteCustomerConfirm.fxml");
    }

    private Customer saveGefcoAsCustomer() {
        Customer gefco = new Customer();
        //gefco.setId(99);
        gefco.setName("Gefco Slovakia s.r.o.");
        gefco.setAddress("SNP 811/168");
        gefco.setCity("Strečno");
        gefco.setPostalCode("013 24");
        Warehouse.getInstance().getDatabaseHandler().saveCustomer(gefco);
        return gefco;
    }

    @FXML
    private void showCompanyInfo() throws IOException {
        Warehouse.getInstance().addController("customerName", "Gefco");
        Warehouse.getInstance().changeScene("CustomerManagement/createNewCustomer.fxml");
    }
}
