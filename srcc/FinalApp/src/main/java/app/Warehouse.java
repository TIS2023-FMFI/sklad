package app;

//<<<<<<< HEAD
import Entity.Material;
import Entity.User;
//=======
//>>>>>>> aea1e46c14cc0de8094550daec6d11bd9a3b5e8c
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
//<<<<<<< HEAD
//import javafx.util.Pair;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//
//public class Warehouse extends Application{
//    //DatabaseHandler databaseHandler;
//    /***
//     * Aktuálne prihlásený používateľ.
//     */
//    User currentUser;
//
//    /***
//     * Mapa, ktorá obsahuje údaje o obsahu sklade. Kľúč prvej mapy je rad v sklade, kľúč druhej mapy
//     * je názov pozície a hodnota je zoznam obsahujúci dvojice materiálu a jeho počtu na tej pozícii.
//     */
//    Map<String,Map<String, List<Pair<Material, Integer>>>> warehouseData;
//
//    private static Stage stage;
//
//    /***
//     * Hlavná metóda, ktorá spustí aplikáciu.
//     * @param primarystage
//     * @throws Exception
//     */
//    @Override
//    public void start(Stage primarystage) throws Exception {
//        //databaseHandler = new DatabaseHandler();
//        stage = primarystage;
//        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("app/login.fxml")));
////=======

import java.io.IOException;
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
    @Override
    public void start(Stage primarystage) throws Exception {
        stage = primarystage;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login.fxml")));
//>>>>>>> aea1e46c14cc0de8094550daec6d11bd9a3b5e8c
        primarystage.setTitle("Skladovací systém");
        primarystage.setScene(new Scene(root, 400, 300));
        primarystage.show();
    }


    /***
     * Metóda, ktorá načíta údaje z databázy do pamäte po úspešnom prihlásení.
     */
    public void loadDb(){
        //warehouseData = DatabaseHandler.getWarehouseData();
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

