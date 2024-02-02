package GUI.UserManagement;

import Entity.Users;
import app.Warehouse;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class saveNewUserController implements Initializable {
    @FXML
    private Button deleteButton;
    @FXML
    private Button saveButton;
    @FXML
    private CheckBox isAdmin;
    @FXML
    private TextField name;
    @FXML
    private TextField password;
    @FXML
    private TextField passwordCheck;
    @FXML
    private Label errorLabel;

    /***
     * User that is being updated, if one is being crated, it is null.
     */
    Users userUpdated;

    /***
     * Initializes the controller class and hides some of the buttons from not admin users.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UserManagementMainController userManagementMainController = (UserManagementMainController)
                Warehouse.getInstance().getController("UserManagementMainController");
        if(!userManagementMainController.creatingNew){
            saveButton.setText("Uložiť zmeny");
            deleteButton.setVisible(true);
            userUpdated = Warehouse.getInstance().getDatabaseHandler().getUser(userManagementMainController.selectedUser);
            name.setText(userUpdated.getName());
            password.setText(userUpdated.getPassword());
            if (userUpdated.getAdmin()){
                isAdmin.setSelected(true);
            }
        }else {
            deleteButton.setVisible(false);
            userUpdated = null;
            name.setText("");
            password.setText("");
            isAdmin.setSelected(false);
        }
    }

    /***
     * Goes back to the user management main scene.
     * @throws IOException if the scene is not found.
     */
    public void goBack() throws IOException {
        Warehouse.getInstance().changeScene("UserManagement/userManagementMain.fxml");
    }

    /***
     * Saves the user to the database.
     */
    public void saveUser() throws IOException {
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
        goBack();
    }

    public void deleteUser() throws IOException {
        if (userUpdated != null){
            Warehouse.getInstance().getDatabaseHandler().deleteUser(userUpdated.getId());
            goBack();
        }
    }
}
