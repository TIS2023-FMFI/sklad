package GUI.Reservations;

import app.Warehouse;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ReservationsMainController implements Initializable {
    @FXML
    private ChoiceBox<String> customer;
    @FXML
    private Button userManagementButton;


    @FXML
    private  Button customerManagementB;
    private  @FXML
    Button addReservationB;
 //   @FXML
 //   private editReservationB;
//    @FXML

    Button layout;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> customers = Warehouse.getInstance().getDatabaseHandler().getCustomersNames();
        customers.remove(Warehouse.getInstance().getDatabaseHandler().getRootCustomer().getName());
        customer.setItems(customers);
        customer.setStyle("-fx-font: 20px 'Calibri';");
        if(customers.size() > 0) {
            customer.setValue(customers.get(0));
        }

        else{
            addReservationB.setDisable(true);
            //editReservationB.setDisable(true);
        }
//        if(! Warehouse.getInstance().getCurrentUser().getAdmin()) {
//            customerManagementB.setVisible(false);
//            addReservationB.setVisible(false);


        if(! Warehouse.getInstance().getCurrentUser().getAdmin()){
            customerManagementB.setVisible(false);
            addReservationB.setVisible(false);
            userManagementButton.setVisible(false);
            layout.setVisible(false);
        }

    }
    public void backToMenu() throws IOException {
        Warehouse.getInstance().changeScene("mainMenu.fxml");
    }

    public void saveCustomerName(){
        Warehouse warehouse = Warehouse.getInstance();
        if(warehouse.getController("customerReservationName") != null){
            warehouse.removeController("customerReservationName");
        }
        warehouse.addController("customerReservationName", customer);
    }

    public void showReservationRecords() throws IOException {
        saveCustomerName();
        Warehouse.getInstance().changeScene("Reservations/reservationsView.fxml");
    }

    public void addNewReservation() throws IOException {
        Warehouse.getInstance().changeScene("Reservations/addReservationFirstForm.fxml");
    }

    public void customerManagement() throws IOException {
        Warehouse.getInstance().changeScene("CustomerManagement/customerManagementMain.fxml");
    }

    public void goToUserManagement() throws IOException {
        Warehouse.getInstance().changeScene("UserManagement/userManagementMain.fxml");
    }

    public void newLayout() throws IOException {
        Warehouse.getInstance().changeScene("WarehouseLayout/createNewLayout.fxml");
    }
}
