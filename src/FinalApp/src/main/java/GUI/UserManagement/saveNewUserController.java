package GUI.UserManagement;

import Entity.Users;
import app.Warehouse;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class saveNewUserController implements Initializable {

    public CheckBox isAdmin;

    public TextField name;
    public TextField password;
    public TextField passwordCheck;
    public Label errorLabel;

    Users userUpdated;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UserManagementMainController userManagementMainController = (UserManagementMainController)
                Warehouse.getInstance().getController("UserManagementMainController");
        if(!userManagementMainController.creatingNew){
            userUpdated = Warehouse.getInstance().getDatabaseHandler().getUser(userManagementMainController.selectedUser);
            name.setText(userUpdated.getName());
            password.setText(userUpdated.getPassword());
            if (userUpdated.getAdmin()){
                isAdmin.setSelected(true);
            }
        }else {
            userUpdated = null;
            name.setText("");
            password.setText("");
            isAdmin.setSelected(false);
        }
    }

    public void goBack() throws IOException {
        Warehouse.getInstance().changeScene("UserManagement/userManagementMain.fxml");
    }

    public void saveUser() {
        String newName = this.name.getText();
        String newPassword = this.password.getText();
        String newPasswordCheck = this.passwordCheck.getText();
        boolean newIsAdmin = this.isAdmin.isSelected();

        if(newName.equals("") || newPassword.equals("") || newPasswordCheck.equals("")){
            errorLabel.setText("Všetky polia musia byť vyplnené");
            return;
        }
        if(!newPassword.equals(newPasswordCheck)){
            errorLabel.setText("Heslá sa nezhodujú");
            return;
        }
        if (userUpdated != null){
            Warehouse.getInstance().getDatabaseHandler().updateUser(userUpdated.getId(), newName, newPassword, newIsAdmin);
        } else {
            Warehouse.getInstance().getDatabaseHandler().addUser(newName, newPassword, newIsAdmin);
        }
    }
}
