package GUI.Reservations;

import Entity.Position;
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

public class WarehouseLayoutRowsReservationsController implements Initializable {
    @FXML
    private HBox rowsContainer;

    private static final int ROW_BUTTON_HEIGHT = 150;
    private static final int ROW_BUTTON_TALL_HEIGHT = 250;
    private static final int ROW_BUTTON_WIDTH = 50;

    private boolean isTall;

    Set<String> changedRows = new HashSet<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Warehouse.getInstance().addController("warehouseLayout", this);
        isTall = false;

        Warehouse.getInstance().initializeWarehouseLayout();
        getChangedRows();
        loadRows();

    }

    public void loadRows() {
        Warehouse warehouse = Warehouse.getInstance();
        List<String> rowNames = warehouse.getRowNames();

        for (String rowName : rowNames) {
            Button rowButton = createRowButton(rowName);

            if(changedRows.contains(rowName)) {
                rowButton.setTextFill(Color.RED);
                System.out.println(rowName);
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

    private Button createRowButton(String rowName) {
        Button rowButton = new Button(rowName);
        rowButton.setPrefWidth(ROW_BUTTON_WIDTH);
        rowsContainer.setAlignment(Pos.BOTTOM_CENTER);
        return rowButton;
    }

    private void addHorizontalSpacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        rowsContainer.getChildren().add(spacer);
    }

    private void setRowButtonSize(Button rowButton) {
        if (isTall) {
            rowButton.setPrefHeight(ROW_BUTTON_TALL_HEIGHT);
        } else {
            rowButton.setPrefHeight(ROW_BUTTON_HEIGHT);
        }
    }

    public void nextToRowLayout() {
        try {
            Warehouse.getInstance().changeScene("Reservations/rowLayoutReservationsForm.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void getChangedRows(){
        List<Position> bestPositions = (List<Position>) Warehouse.getInstance().getController("bestLowPositions");
        bestPositions.addAll((List<Position>) Warehouse.getInstance().getController("bestTallPositions"));

        for(Position p : bestPositions){

            String row = p.getName().substring(0, 1);

            boolean even = Integer.parseInt(p.getName().substring(1,4)) % 2 == 0;
            if(even){ row += "p";}
            else{ row += "n";}

            changedRows.add(row);
        }
    }

    public void backToMenu() throws IOException {
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.deleteWarehouseLayoutInstance();
        warehouse.removeController("warehouseLayout");
        warehouse.changeScene("Reservations/addReservationSecondForm.fxml");
    }
}
