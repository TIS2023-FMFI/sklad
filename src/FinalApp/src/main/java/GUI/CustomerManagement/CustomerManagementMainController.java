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
    private ChoiceBox<String> customer;
    @FXML
    private Label errorMessage;
    @FXML
    private Button delete;
    @FXML
    private Button showCustomer;
    private Customer root;

    /***
     * Method to initialize the customer management main controller and set the customer choice box
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Customer us = Warehouse.getInstance().getDatabaseHandler().getRootCustomer();
        root = us;
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

    /***
     * Method that saves customer name to the warehouse
     */
    public void saveCustomerName(){
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.addController("customerName", customer.getValue());
    }

    /***
     * Method to show information about the customer
     * @throws IOException if the scene is not found
     */
    public void showInformation() throws IOException {
        Warehouse warehouse = Warehouse.getInstance();
        if(warehouse.getController("customerName") != null){
            warehouse.removeController("customerName");
        }
        warehouse.addController("customerName", customer.getValue());
        Warehouse.getInstance().changeScene("CustomerManagement/createNewCustomer.fxml");
    }

    /***
     * Method that takes user to the main reservation scene
     * @throws IOException if the scene is not found
     */
    public void backToMainReservation() throws IOException {
        Warehouse.getInstance().changeScene("Reservations/reservationsMain.fxml");
    }

    /***
     * Method that removes edited customer and takes user to the create new customer scene
     * @throws IOException if the scene is not found
     */
    public void createNewCustomerScene() throws IOException {
        if(Warehouse.getInstance().getController("customerName") != null){
            Warehouse.getInstance().removeController("customerName");
        }
        Warehouse.getInstance().changeScene("CustomerManagement/createNewCustomer.fxml");
    }

    /***
     * Method that takes user to the delete customer scene
     * @throws IOException if the scene is not found
     */
    public void deleteCustomerScene() throws IOException {
        saveCustomerName();
        Warehouse.getInstance().changeScene("CustomerManagement/deleteCustomerConfirm.fxml");
    }

    @FXML
    private void showCompanyInfo() throws IOException {
        Warehouse.getInstance().addController("customerName", root.getName());
        Warehouse.getInstance().changeScene("CustomerManagement/createNewCustomer.fxml");
    }
}
