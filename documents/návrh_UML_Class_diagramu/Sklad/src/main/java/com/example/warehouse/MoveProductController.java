package com.example.warehouse;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.ArrayList;

public class MoveProductController {
    @FXML
    private TextField position;

    @FXML
    private ChoiceBox productsOnPallet;

    public void confirmInitialPosition() throws IOException {
        if (!checkIfPositionIsCorrect(position.getText())){

        }
        else {
            System.out.println(position.getText());
            Warehouse warehouse = new Warehouse();
            warehouse.changeScene("chooseProductToMoveForm.fxml");
            //fillProductList();
        }
    }

    public void backToMenu() throws IOException {
        Warehouse warehouse = new Warehouse();
        warehouse.changeScene("mainMenu.fxml");
    }


    private boolean checkIfPositionIsCorrect(String position){
        return true;
    }

    private void fillProductList(){
        ObservableList<String> options = FXCollections.observableArrayList("Option 1", "Option 2", "Option 3", "Option 4");
        productsOnPallet.setItems(options);
    }

    public void backToInitialPosition() throws IOException {
        Warehouse warehouse = new Warehouse();
        warehouse.changeScene("moveFromForm.fxml");
    }

    public void confirmProductToMove() throws IOException {
        Warehouse warehouse = new Warehouse();
        warehouse.changeScene("chooseFinalPositionForm.fxml");
    }

    public void backToProductChoice() throws IOException {
        Warehouse warehouse = new Warehouse();
        warehouse.changeScene("chooseProductToMoveForm.fxml");
    }

    /***
     * Zmení údaje v databáze.
     * @throws IOException
     */
    public void confirmFinalPosition() throws IOException {
        Warehouse warehouse = new Warehouse();
        warehouse.changeScene("movingConfirmationForm.fxml");
    }
}
