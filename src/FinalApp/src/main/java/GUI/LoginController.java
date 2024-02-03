package GUI;

import Entity.Customer;
import app.CheckPositions;
import app.DatabaseHandler;
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
            Warehouse warehouse = Warehouse.getInstance();
            if (username.getText() == null || username.getText().trim().isEmpty()) {
                wrongLogin.setText("Nezadali ste používateľské meno");
            }
            if (password.getText() == null || password.getText().trim().isEmpty()) {
                wrongLogin.setText("Nezadali ste používateľské heslo");
            }
            warehouse.setCurrentUser(DatabaseHandler.checkUser(username.getText(), password.getText()));


            warehouse.loadDb();
            warehouse.changeScene("mainMenu.fxml");

            CheckPositions checkPositions = new CheckPositions();
            if(!checkPositions.allPositionsCorrect()){
                checkPositions.createNewWindow();
            }

            if (Warehouse.getInstance().getDatabaseHandler().getRootCustomer() == null) {
                Customer gefco = new Customer();
                gefco.setName("Gefco Slovakia s.r.o.");
                gefco.setAddress("SNP 811/168");
                gefco.setCity("Strečno");
                gefco.setPostalCode("013 24");
                gefco.setRoot(true);
                Warehouse.getInstance().getDatabaseHandler().saveCustomer(gefco);
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            wrongLogin.setText(e.getMessage());
        }
    }
}