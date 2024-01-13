package GUI.WarehouseLayout;

import app.Warehouse;
import Entity.Position;

import java.io.IOException;

public class RowLayoutController {
    public void backToRows() throws IOException{
        Warehouse.getInstance().changeScene("WarehouseLayout/warehouseLayoutRowsForm.fxml");
    }

    // zobrazí grid pre jednotlivý rad
    public void showRow(String row){
    }

    // zobrazí informácie o zvolenej pozícií
    public void showPositionInformations(Position position){

    }
}
