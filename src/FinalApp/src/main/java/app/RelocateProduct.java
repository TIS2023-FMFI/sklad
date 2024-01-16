package app;

import Entity.Material;
import Entity.Pallet;
import Entity.Position;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RelocateProduct {

    /** skontroluje, ci na danu poziciu sa moze preskladnit tovar**/
    public boolean checkPositionIsEmpty(Position p){return false;};
    public void relocateProduct(Position finalPos, Position initialPos,
                                String product, int quantity, String palletFrom, String palletTo){

        addItem(finalPos, product, quantity, palletTo, palletFrom);
        //removeItem(initialPos, product, quantity, palletFrom);
    }

    private void removeItem(Position initialPos, String product, int quantity, String palletFrom){
        OrderProduct op = new OrderProduct();
        DatabaseHandler db = Warehouse.getInstance().getDatabaseHandler();
        ObservableList<Map<String, String>> items = FXCollections.observableArrayList();
        items.add(Map.of("Pozícia", initialPos.getName(), "PNR", palletFrom,
                "Materiál", product, "Počet", String.valueOf(quantity)));

        op.removeOrderedItems(items);
    }

    private void addItem(Position finalPos, String product, int quantity, String palletTo, String palletFrom){
        //zistit v pamati ci je na tej pozicii dana paleta
        //Map<Position, Map<Pallet, Map<Material, Integer>>> getPalletsOnPosition
        

        //ak nie je, vytvorit novu paletu

        //ak je, skontrolovat ci an nej je taky produkt
        //ak nie je, pridat ho

        //ak je, zvysit pocet kusov


    }


}
