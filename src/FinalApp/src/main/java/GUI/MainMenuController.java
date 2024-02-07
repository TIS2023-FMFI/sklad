package GUI;
import app.Warehouse;

import java.io.IOException;

public class MainMenuController {
    /***
     * This method is used to show the warehouse layout
     * @throws IOException if the fxml file is not found
     */
    public void showWarehouseLayout() throws IOException {
        Warehouse.getInstance().initializeWarehouseLayout();
        Warehouse.getInstance().changeScene("WarehouseLayout/warehouseLayoutRowsForm.fxml");
    }

    /***
     * This method is used to show the store in product form
     * @throws IOException if the fxml file is not found
     */
    public void storeInProduct() throws IOException {
        Warehouse.getInstance().initializeStoreInProduct();
        Warehouse.getInstance().changeScene("StoreInProduct/customerTruckNumberForm.fxml");
    }

    /***
     * This method is used to show the store out product form
     * @throws IOException if the fxml file is not found
     */
    public void relocateProduct() throws IOException {
        Warehouse.getInstance().changeScene("RelocateProduct/moveProductFromPositionForm.fxml");
    }

    /***
     * This method is used to show the order product form
     * @throws IOException if the fxml file is not found
     */
    public void placeOrder() throws IOException {
        Warehouse.getInstance().changeScene("OrderProduct/orderCustomerSelectionForm.fxml");
    }

    /***
     * This method is used to show the statistics form
     * @throws IOException if the fxml file is not found
     */
    public void showStatistics() throws IOException {
        Warehouse.getInstance().changeScene("Statistics/statisticsMainPageForm.fxml");
    }

    /***
     * This method is used to show the reservations and user/customer management form
     * @throws IOException if the fxml file is not found
     */
    public void reservationsMain() throws IOException {
        Warehouse.getInstance().changeScene("Reservations/reservationsMain.fxml");
    }

    /***
     * This method is used to log in the app.
     * @throws IOException if the fxml file is not found
     */
    public void logout() throws IOException {
         Warehouse.getInstance().changeScene("login.fxml");
    }
}
