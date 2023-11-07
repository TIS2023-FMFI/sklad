package com.example.warehouse;

import java.io.IOException;
import java.util.Map;

public class WarehouseLayoutController {
    // zobrazí grid pre jednotlivý rad
    public void showRow(String row){

    }

    // zobrazí informácie o zvolenej pozícií
    public void showPositionInformations(Position position){

    }


    public void backToMenu() throws IOException {
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.changeScene("mainMenu.fxml");
    }

    public void backToRows() throws IOException{
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.changeScene("warehouseLayoutRows.fxml");
    }
}
