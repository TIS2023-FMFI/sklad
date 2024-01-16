package GUI;

import Entity.Position;
import app.DatabaseHandler;
import app.RelocateProduct;
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
                wrongLogin.setText("Nezadali ste používateľské meno");
            }
            if (password.getText() == null || password.getText().trim().isEmpty()){
                wrongLogin.setText("Nezadali ste používateľské heslo");
            }
            Warehouse.getInstance().setCurrentUser(DatabaseHandler.checkUser(username.getText(), password.getText()));
            Warehouse.getInstance().loadDb();


            DatabaseHandler db = new DatabaseHandler();
            Warehouse w = Warehouse.getInstance();
            RelocateProduct relocateProduct = new RelocateProduct();
            Position initPos = db.getPosition("A0001");
            Position finalPos = db.getPosition("A0100");
            relocateProduct.relocateProduct(finalPos, initPos,
                    "Test material", 2, "2000", "2222");


            Warehouse.getInstance().changeScene("mainMenu.fxml");
        }
        catch (Exception e){
            wrongLogin.setText(e.getMessage());
        }
    }
}