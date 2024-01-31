package app;

import Entity.Material;
import Entity.Pallet;
import Entity.Position;
import Entity.Users;

import Exceptions.FileNotFound;
import Exceptions.WrongStringFormat;
import com.jfoenix.controls.JFXButton;
import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class Warehouse extends Application {
    private static Warehouse INSTANCE;
    private static Stage stage;
    private DatabaseHandler databaseHandler;
    private WarehouseLayout warehouseLayout;
    private StoreInProduct storeInProduct;
    private static final String FILE_NAME = "warehouse_layout.txt";


    /***
     * Currently logged-in user.
     */

    public Users currentUser;
    private final Map<String, Object> controllers = new HashMap<>();
  
    /***
     * Map that maps rows in the warehouse to map of and its positions.
     */
    private Map<String, Map<Integer, List<Position>>> positionsInRows;

    /***
     * Map that maps rows in the warehouse to map of and its positions which are grouped to the foursome based on their names.
     */
    private Map<String, Map<Integer, Map<Integer, List<Position>>>> positionsInGroups;

    /***
     * Map that stores pallets on positions.
     */
    private Map<Position, Map<Pallet, Map<Material, Integer>>> palletsOnPosition;

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

    /***
     * The main method that launches and initializes the application.
     *
     * @param primaryStage The primary stage for the JavaFX application.
     * @throws Exception If there is an error during application startup.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login.fxml")));
        primaryStage.setTitle("Skladovací systém");
        primaryStage.setScene(new Scene(root, 700, 500));
        stage.setResizable(false);
        primaryStage.show();
    }

    /***
     * Method, that changes the currently displayed scene based in the fxml file provided.
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

        stage.setResizable(false);

        centerStage(stage);
    }

    /***
     * Method that centers the stage in the middle of the computer screen.
     * @param stage The JavaFX stage to be centered.
     */
    private void centerStage(Stage stage) {
        double screenWidth = javafx.stage.Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = javafx.stage.Screen.getPrimary().getVisualBounds().getHeight();

        double centerX = screenWidth/2 - stage.getWidth()/2;
        double centerY = screenHeight/2 - stage.getHeight()/2;

        stage.setX(centerX);
        stage.setY(centerY);
    }

    /***
     * Method, that loads data to the memory after a successful login.
     */
    public void loadDb(){
        positionsInRows = databaseHandler.loadPositionsInRows();
        palletsOnPosition = databaseHandler.loadPalletsOnPositions();
        positionsInGroups = databaseHandler.getPositionInGroups();
    }
  
    public Map<String, Map<Integer, List<Position>>> getPositionsInRows() {
        return positionsInRows;
    }

    public Map<String, Map<Integer, Map<Integer, List<Position>>>> getPositionsInGroups() {
        return positionsInGroups;
    }
    public Map<Position, Map<Pallet, Map<Material, Integer>>> getPalletsOnPositionMap() {
        return palletsOnPosition;
    }

    public void setCurrentUser(Users currentUser) {
        this.currentUser = currentUser;
    }

    public Users getCurrentUser() {
        return currentUser;
    }

    public DatabaseHandler getDatabaseHandler() {
        return databaseHandler;
    }

    public void initializeWarehouseLayout(){
        warehouseLayout = new WarehouseLayout();
    }

    public WarehouseLayout getWarehouseLayoutInstance() {
        return warehouseLayout;
    }

    public void deleteWarehouseLayoutInstance(){
        warehouseLayout = null;
    }

    public StoreInProduct getStoreInInstance() {
        return storeInProduct;
    }

    public void initializeStoreInProduct(){
        storeInProduct = new StoreInProduct();
    }

    public void deleteStoreInProductInstance(){
        storeInProduct = null;
    }

    public Object getController(String name) {
        return controllers.get(name);
    }
    public void addController(String name, Object controler) {
        controllers.put(name, controler);
    }

    public void removeController(String name){
        controllers.remove(name);
    }

    public List<String> getRowNames(){
        return new ArrayList<>(positionsInRows.keySet());
    }

    public Map<Integer, List<Position>> getRowMap(String rowName){
        return positionsInRows.get(rowName);
    }

    public boolean isPalletOnPosition(Position position){
        return palletsOnPosition.get(position).isEmpty();
    }

    public Map<Pallet, Map<Material, Integer>> getPalletsOnPosition(Position position){
        return palletsOnPosition.get(position);
    }

    public static Stage getStage() {
        return stage;
    }

    public JFXButton createStyledButton(String buttonText, String backgroundColor, String textColor, double width,
                                         double height, int fontSize, boolean isTextBold) {
        JFXButton button = new JFXButton(buttonText);

        BackgroundFill backgroundFill = new BackgroundFill(Color.web(backgroundColor), CornerRadii.EMPTY, javafx.geometry.Insets.EMPTY);
        Background background = new Background(backgroundFill);
        button.setBackground(background);

        button.setTextFill(Color.web(textColor));

        button.setRipplerFill(Color.WHITE);

        button.setMinWidth(width);
        button.setMinHeight(height);

        FontWeight fontWeight = isTextBold ? FontWeight.BOLD : FontWeight.NORMAL;
        button.setFont(Font.font("Calibri", fontWeight, fontSize));

        return button;
    }

    public Label createStyledLabel(String labelText, int fontSize) {
        Label label = new Label(labelText);

        label.setTextFill(Color.BLACK);
        label.setFont(Font.font("Calibri", FontWeight.NORMAL, fontSize));

        return label;
    }

    protected boolean loadPosition() {
        try {
            LoadPositions load = new LoadPositions(FILE_NAME);
            load.addPositions();
            return load.saveToDB();
        } catch (FileNotFound | WrongStringFormat e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        boolean loadNewPositions = false;
        if(loadNewPositions) {
            getInstance().loadPosition();
        }
        launch();
    }
}
