package app;


import Entity.DatabaseHandler;
import Entity.Position;
import Entity.Users;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class Warehouse extends Application {
    private static Warehouse INSTANCE;

    Map<String, Object> controllers = new HashMap<>();

    /***
     * Method that always returns the same instance of the Warehouse class.
     * @return Instance of the Warehouse class.
     */
    public static Warehouse getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Warehouse();
        }
        return INSTANCE;
    }

    public Warehouse() {
        try {
            databaseHandler = new DatabaseHandler();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Stage stage;
    private DatabaseHandler databaseHandler;

    /***
     * Currently logged-in user.
     */
    private Users currentUser;

    public void setCurrentUser(Users currentUser) {
        this.currentUser = currentUser;
    }

    /***
     * Map that maps rows in the warehouse to a list of positions in that row.
     */
    private Map<String, List<Position>> warehouseData;

    /***
     //     * Main method, that runs the application.
     //     * @param primarystage
     //     * @throws Exception
     //     */
    @Override
    public void start(Stage primarystage) throws Exception {
        stage = primarystage;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login.fxml")));
        primarystage.setTitle("Skladovací systém");
        primarystage.setScene(new Scene(root, 400, 300));
        primarystage.show();
    }


    /***
     * Method, that loads data to the memory after a successful login.
     */
    public void loadDb(){
        warehouseData = databaseHandler.getWarehouseData();
    }

    /***
     * Metho, that changes the currently displayed scene based in the fxml file provided.
     * @param fxml Fxml that defines the new scene.
     * @throws IOException If there is an issue displaying the new scene.
     */
    public void changeScene(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        Parent pane = loader.load();

        double newWidth = pane.prefWidth(-1);
        double newHeight = pane.prefHeight(-1);

        stage.getScene().setRoot(pane);

        stage.getScene().getWindow().setWidth(newWidth);
        stage.getScene().getWindow().setHeight(newHeight);
    }

    public Users getCurrentUser() {
        return currentUser;
    }


    public static void main(String[] args) {
        launch();
    }

    public DatabaseHandler getDatabaseHandler() {
        return databaseHandler;
    }

    public void addController(String name, Object controler) {
        controllers.put(name, controler);
    }
}
