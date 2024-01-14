package GUI.WarehouseLayout;

import app.Warehouse;
import Entity.Position;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class RowLayoutController implements Initializable {
    @FXML
    private VBox positionsVBox;

    private final String RED_COLOR = "#FF0000";
    private final String ORANGE_COLOR = "#FFA500";
    private final String GREEN_COLOR = "#228B22";

    private final int POSITION_BUTTON_WIDTH = 30;
    private final int POSITION_BUTTON_HEIGHT = 30;
    private final int TALL_POSITION_BUTTON_HEIGHT = 45;

    private final int POSITION_BUTTON_SPACING = 5;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Warehouse.getInstance().addController("rowLayout", this);
        createGrid();
    }

    public void createGrid(){
        Map<Integer, List<Position>> shelfAndItsPositions = Warehouse.getInstance().getWarehouseLayoutInstance().getShelfAndItsPositions();
        positionsVBox.setSpacing(POSITION_BUTTON_SPACING);
        for (List<Position> positions : shelfAndItsPositions.values()){
            positionsVBox.getChildren().add(createShelf(positions));
        }
    }

    public HBox createShelf(List<Position> positions){
        HBox shelf = new HBox();
        shelf.setSpacing(POSITION_BUTTON_SPACING);
        for (Position position : positions){
            Button positionButton = new Button();
            positionButton.setPrefWidth(POSITION_BUTTON_WIDTH);
            if (position.isTall()){
                positionButton.setPrefHeight(TALL_POSITION_BUTTON_HEIGHT);
            }
            else {
                positionButton.setPrefHeight(POSITION_BUTTON_HEIGHT);
            }
            positionButton.setStyle("-fx-background-color:" + getColor(position) + ";"
                    + "-fx-border-color: black;"
                    + "-fx-border-width: 1;");
            shelf.getChildren().add(positionButton);
        }
        return shelf;
    }

    public String getColor(Position position){
        if (!Warehouse.getInstance().isPalletOnPosition(position)){
            return RED_COLOR;
        }
        else if (Warehouse.getInstance().getDatabaseHandler().isPositionReserved(position.getName())){
            return ORANGE_COLOR;
        }
        return GREEN_COLOR;
    }

    public void backToRows() throws IOException{
        Warehouse.getInstance().removeController("rowLayout");
        Warehouse.getInstance().changeScene("WarehouseLayout/warehouseLayoutRowsForm.fxml");
    }

}
