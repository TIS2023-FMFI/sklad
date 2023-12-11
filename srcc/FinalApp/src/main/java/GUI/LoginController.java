package GUI;

import app.DatabaseHandler;
import Exceptions.EmptyPassword;
import Exceptions.EmptyUsername;
import app.Warehouse;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML
    private Label wrongLogin;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    public void login(){
        try {
            if (username.getText() == null || username.getText().trim().isEmpty()) {
                throw new EmptyUsername();
            }
            if (password.getText() == null || password.getText().trim().isEmpty()){
                throw new EmptyPassword();
            }
            Warehouse.getInstance().setCurrentUser(DatabaseHandler.checkUser(username.getText(), password.getText()));
            Warehouse.getInstance().loadDb();
            Warehouse.getInstance().changeScene("mainMenu.fxml");
        }
        catch (Exception e){
            wrongLogin.setText(e.getMessage());
        }
    }
}
