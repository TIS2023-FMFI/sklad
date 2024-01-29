package GUI.Reservations;

import Entity.Position;
import GUI.WarehouseLayout.WarehouseLayoutRowsController;
import app.Warehouse;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class WarehouseLayoutRowsReservationsController extends WarehouseLayoutRowsController implements Initializable {
    Set<String> changedRows = new HashSet<>();
    Set<Position> positionsToSave;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        isTall = false;
        positionsToSave = new HashSet<>();

        Warehouse.getInstance().initializeWarehouseLayout();
        positionsToSave = getPostionsToSave();
      
        getChangedRows();
        loadRows();
    }

    @Override
    public void loadRows() {
        Warehouse warehouse = Warehouse.getInstance();
        List<String> rowNames = warehouse.getRowNames();

        for (String rowName : rowNames) {
            Button rowButton = createRowButton(rowName);

            if(changedRows.contains(rowName)) {
                rowButton.setTextFill(Color.RED);
            }

            if (rowName.equals("Cp")) {
                isTall = true;
            }

            if (rowName.endsWith("p")) {
                addHorizontalSpacer();
            }

            setRowButtonSize(rowButton);

            rowButton.setOnAction(event -> {
                var add = Warehouse.getInstance().getRowMap(rowName);
                Warehouse.getInstance().getWarehouseLayoutInstance().setShelfAndItsPositions(add);
                nextToRowLayout();
            });

            rowsContainer.getChildren().add(rowButton);
        }
    }

    @Override
    public void nextToRowLayout() {
        try {
            Warehouse.getInstance().changeScene("Reservations/rowLayoutReservationsForm.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected Set<Position> getPostionsToSave(){
        if(Warehouse.getInstance().getController("positionsToSave") != null){
            return (Set<Position>) Warehouse.getInstance().getController("positionsToSave");
        }
        List<Position> bestPositions = (List<Position>) Warehouse.getInstance().getController("bestLowPositions");
        bestPositions.addAll((List<Position>) Warehouse.getInstance().getController("bestTallPositions"));
        Set<Position> result = new HashSet<>();
        for(Position p : bestPositions){
            result.add(p);
        }
        Warehouse.getInstance().addController("positionsToSave", result);
        return result;
    }

    /**
     * function finds regals in which is some position choose to be reserved
     */
    public void getChangedRows(){
        for(Position p : positionsToSave){
            String row = p.getName().substring(0, 1);
            boolean even = Integer.parseInt(p.getName().substring(1,4)) % 2 == 0;
            if(even){ row += "p";}
            else{ row += "n";}
            changedRows.add(row);
        }
    }
    @Override
    public void backToMenu() throws IOException {
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.deleteWarehouseLayoutInstance();
        warehouse.removeController("warehouseLayout");
        warehouse.changeScene("Reservations/addReservationSecondForm.fxml");
    }
}
