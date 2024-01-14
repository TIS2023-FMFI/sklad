package GUI.WarehouseLayout;

import app.Warehouse;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class WarehouseLayoutRowsController implements Initializable {
    @FXML
    private HBox rowsContainer;

    private final int ROW_BUTTON_HEIGHT = 200;
    private final int ROW_BUTTON_WIDTH = 50;

    private final int ROW_BUTTON_SPACING = 10;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Warehouse.getInstance().addController("warehouseLayout", this);
        loadRows();
    }

    public void loadRows(){
        Warehouse warehouse = Warehouse.getInstance();
        List<String> rowNames = warehouse.getRowNames();

        for (String rowName : rowNames){
            Button rowButton = new Button(rowName);

            rowButton.setPrefWidth(ROW_BUTTON_WIDTH);
            rowButton.setPrefHeight(ROW_BUTTON_HEIGHT);
            rowsContainer.setSpacing(ROW_BUTTON_SPACING);
            rowsContainer.setAlignment(Pos.CENTER);

            rowButton.setOnAction(event -> {
                    warehouse.getWarehouseLayoutInstance().setShelfAndItsPositions(warehouse.getRowMap(rowName));
                    nextToRowLayout();
                    }
            );
            rowsContainer.getChildren().add(rowButton);

        }
    }

    public void nextToRowLayout(){
        try {
            Warehouse.getInstance().changeScene("WarehouseLayout/rowLayoutForm.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void backToMenu() throws IOException {
        Warehouse warehouse = Warehouse.getInstance();

        warehouse.deleteWarehouseLayoutInstance();
        warehouse.removeController("warehouseLayout");
        warehouse.changeScene("mainMenu.fxml");
    }
}
