package app;

import Entity.Material;
import Entity.Pallet;
import Entity.Position;
import Exceptions.MaterialNotAvailable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RelocateProduct {

    public void relocatePallet(Position initialPos, List<String> finalPos, String palletFrom){
        DatabaseHandler db = Warehouse.getInstance().getDatabaseHandler();
        List<Position> posList = db.getPositionsOfPallet(palletFrom);

        for (String pos : finalPos){
            db.updatePalletPosition(palletFrom, pos);
        }
        Pallet beingMoved = Warehouse.getInstance().getPalletsOnPositionMap().get(initialPos).keySet().stream()
                .filter(p -> p.getPnr().equals(palletFrom)).findFirst().get();
        var materialsStored = Warehouse.getInstance().getPalletsOnPositionMap().get(initialPos).remove(beingMoved);

        for (Position pos : posList){
            Warehouse.getInstance().getPalletsOnPositionMap().get(pos).remove(beingMoved);
        }

        for (String pos : finalPos){
            Position newPos = db.getPosition(pos);
            Warehouse.getInstance().getPalletsOnPositionMap().get(newPos).put(beingMoved, materialsStored);
        }
    }


    public void relocateProduct(String finalPos, Position initialPos,
                                String product, int quantity, String palletFrom, String palletTo){

        DatabaseHandler db = Warehouse.getInstance().getDatabaseHandler();
        Position finalPosition = db.getPosition(finalPos);
        Pallet pallet = null;
        for (Pallet p : Warehouse.getInstance().getPalletsOnPositionMap().get(finalPosition).keySet()){
            if (p.getPnr().equals(palletTo)){
                pallet = p;
                break;
            }
        }

        if (pallet == null){
            pallet = new Pallet();
            pallet.setPnr(palletTo);
            pallet.setWeight(500);
            pallet.setDamaged(false);
            pallet.setNote("");
            pallet.setIdUser(Warehouse.getInstance().currentUser.getId());
            pallet.setType("europaleta");
            pallet.setDateIncome(Date.valueOf(LocalDate.now()));
            Warehouse.getInstance().getPalletsOnPositionMap().get(finalPosition)
                    .put(pallet, new HashMap<Material, Integer>());
            db.addPallet(pallet, finalPosition);
        }

        addItem(finalPosition, product, quantity, pallet);
        removeItem(initialPos, product, quantity, palletFrom);
    }

    private void removeItem(Position initialPos, String product, int quantity, String palletFrom){
        OrderProduct op = new OrderProduct();
        DatabaseHandler db = Warehouse.getInstance().getDatabaseHandler();
        ObservableList<Map<String, String>> items = FXCollections.observableArrayList();
        items.add(Map.of("Pozícia", initialPos.getName(), "PNR", palletFrom,
                "Materiál", product, "Počet", String.valueOf(quantity)));

        op.removeOrderedItems(items);
    }

    private void addItem(Position finalPos, String product, int quantity, Pallet palletTo) {
        DatabaseHandler db = Warehouse.getInstance().getDatabaseHandler();
        Material material;
        try {
            material = db.getMaterial(product);
        } catch (MaterialNotAvailable e) {
            return;
        }
        var matOnPallet = Warehouse.getInstance().getPalletsOnPositionMap().get(finalPos).get(palletTo);
        for (Material m : matOnPallet.keySet()) {
            if (m.getName().equals(product)) {
                int oldQuantity = matOnPallet.get(m);
                db.persistMaterialOnPallet(palletTo, m, quantity);
                return;
            }
        }
        Warehouse.getInstance().getPalletsOnPositionMap().get(finalPos).get(palletTo).put(material, quantity);
        db.persistMaterialOnPallet(palletTo, material, quantity);
    }
}
