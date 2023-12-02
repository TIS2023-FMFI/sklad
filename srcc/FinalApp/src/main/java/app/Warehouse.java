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
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Warehouse extends Application {
    private static Warehouse INSTANCE;
    public static Warehouse getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Warehouse();
        }
        return INSTANCE;
    }
    private static Stage stage;

    private DatabaseHandler databaseHandler;

    /***
     * Currently logged-in user.
     */
    Users currentUser;

    /***
     * Map that maps rows in the warehouse to a list of positions in that row.
     */
    Map<String, List<Position>> warehouseData;

    /***
     //     * Main method, that runs the application.
     //     * @param primarystage
     //     * @throws Exception
     //     */
    @Override
    public void start(Stage primarystage) throws Exception {
        databaseHandler = new DatabaseHandler();
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
     * Metóda, ktorá zmení scénu na základe fxml súboru.
     * @param fxml Fxml súbor s novou scénou.
     * @throws IOException Ak sa nepodarí načítať súbor.
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


    public static void main(String[] args) {
        launch();
    }
}

