package GUI.UserManagement;

import Entity.Users;
import app.Warehouse;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class UserManagementMainController implements Initializable {
    @FXML
    public ChoiceBox<String> usersChoiceBox;
    @FXML
    public Label errorMessage;
    String selectedUser;
    boolean creatingNew = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Users> users = Warehouse.getInstance().getDatabaseHandler().getUsers();
        users.remove(Warehouse.getInstance().currentUser);
        usersChoiceBox.setItems(FXCollections.observableArrayList(users.stream().map(Users::getName).toList()));
        if(users.size() > 0) {
            usersChoiceBox.setValue(users.get(0).getName());
        }
    }

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

    public void backToMainReservation() throws IOException {
        Warehouse.getInstance().changeScene("Reservations/reservationsMain.fxml");
    }
    public void createNewUser() throws IOException {
        creatingNew = true;
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.addController("UserManagementMainController", this);
        Warehouse.getInstance().changeScene("UserManagement/saveNewUserForm.fxml");
    }

}
