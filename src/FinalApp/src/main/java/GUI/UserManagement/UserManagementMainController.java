package GUI.UserManagement;

import Entity.Users;
import app.Warehouse;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UserManagementMainController implements Initializable {
    @FXML
    private ChoiceBox<String> usersChoiceBox;
    @FXML
    private Label errorMessage;
    /***
     * Name of the user that is being updated, if one is being crated, it is null.
     */
    String selectedUser;
    /***
     * Variable that tells us if we are creating a new user or updating an existing one.
     */
    boolean creatingNew = false;

    /***
     * Method, that initializes the controller class and fills the choice box with users.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Users> users = Warehouse.getInstance().getDatabaseHandler().getUsers();
        usersChoiceBox.setItems(FXCollections.observableArrayList(users.stream().map(Users::getName).toList()));
        if(users.size() > 0) {
            usersChoiceBox.setValue(users.get(0).getName());
        }
    }

    /***
     * Method, that fills text fields with data of the selected user.
     * @throws IOException chyba pri načítaní fxml súboru
     */
    public void showInformation() throws IOException {
        if(usersChoiceBox.getValue() == null){
            errorMessage.setText("Musíte vybrať používateľa");
            return;
        }
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.addController("UserManagementMainController", this);
        creatingNew = false;
        selectedUser = usersChoiceBox.getValue();
        Warehouse.getInstance().changeScene("UserManagement/saveNewUserForm.fxml");
    }

    /***
     * Method, that takes user a step back.
     */
    public void backToMainReservation() throws IOException {
        Warehouse.getInstance().changeScene("Reservations/reservationsMain.fxml");
    }

    /***
     * Method, that confirms the creation of the selected user.
     */
    public void createNewUser() throws IOException {
        creatingNew = true;
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.addController("UserManagementMainController", this);
        Warehouse.getInstance().changeScene("UserManagement/saveNewUserForm.fxml");
    }

}
