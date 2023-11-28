package GUI;

import app.Warehouse;

import java.io.IOException;

public class RowLayoutController {
    public void backToRows() throws IOException{
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.changeScene("warehouseLayoutRows.fxml");
    }

    // zobrazí grid pre jednotlivý rad
    public void showRow(String row){
    }

    // zobrazí informácie o zvolenej pozícií
    public void showPositionInformations(Position position){

    }
}
