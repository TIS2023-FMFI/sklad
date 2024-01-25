package GUI.WarehouseLayout;

import app.Warehouse;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class WarehouseLayoutRowsController implements Initializable {
    @FXML
    private HBox rowsContainer;

    private static final int ROW_BUTTON_HEIGHT = 150;
    private static final int ROW_BUTTON_TALL_HEIGHT = 250;
    private static final int ROW_BUTTON_WIDTH = 50;

    private boolean isTall;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Warehouse.getInstance().addController("warehouseLayout", this);
        isTall = false;
        loadRows();
    }

    public void loadRows() {
        Warehouse warehouse = Warehouse.getInstance();
        List<String> rowNames = warehouse.getRowNames();

        for (String rowName : rowNames) {
            Button rowButton = createRowButton(rowName);

            if (rowName.equals("Cp")) {
                isTall = true;
            }

            if (rowName.endsWith("p")) {
                addHorizontalSpacer();
            }

            setRowButtonSize(rowButton);

            rowButton.setOnAction(event -> {
                warehouse.getWarehouseLayoutInstance().setShelfAndItsPositions(warehouse.getRowMap(rowName));
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
