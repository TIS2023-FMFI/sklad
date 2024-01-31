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

    /***
     * This method moeves the entire pallet to the new position.
     * @param initialPos Position from which the pallet is moved.
     * @param finalPos Position to which the pallet is moved.
     * @param palletFrom Pallet number of the pallet that is moved.
     */
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

    /***
     * This method moves the product from one pallet to another.
     * @param finalPoses Positions to which the product is moved.
     * @param initialPos Position from which the product is moved.
     * @param product Name of the product that is moved.
     * @param quantity Quantity of the product that is moved.
     * @param palletFrom Pallet number of the pallet from which the product is moved.
     * @param palletTo Pallet number of the pallet to which the product is moved.
     */
    public void relocateProduct(List<String> finalPoses, Position initialPos,
                                String product, int quantity, String palletFrom, String palletTo){
        System.out.println("finalPoses: " + finalPoses);
        String finalPos = finalPoses.get(0);
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
            pallet.setWeight(0);
            pallet.setDamaged(false);
            pallet.setNote("");
            pallet.setIdUser(Warehouse.getInstance().currentUser.getId());
            pallet.setType("europaleta");
            pallet.setDateIncome(Date.valueOf(LocalDate.now()));
            pallet.setNumberOfPositions(1);
            Warehouse.getInstance().getPalletsOnPositionMap().get(finalPosition)
                    .put(pallet, new HashMap<Material, Integer>());
            db.addPallet(pallet, finalPosition);
        }

        addItem(finalPoses, product, quantity, pallet);
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

    private void addItem(List<String> finalPoses, String product, int quantity, Pallet palletTo) {
        DatabaseHandler db = Warehouse.getInstance().getDatabaseHandler();
        Material material;
        try {
            material = db.getMaterial(product);
        } catch (MaterialNotAvailable e) {
            return;
        }

        for (var pos:Warehouse.getInstance().getPalletsOnPositionMap().keySet()) {
            if (finalPoses.contains(pos.getName())) {
                if (!Warehouse.getInstance().getPalletsOnPositionMap().get(pos).get(palletTo).containsKey(material)) {
                    Warehouse.getInstance().getPalletsOnPositionMap().get(pos).get(palletTo).put(material, quantity);
                }else{
                    Warehouse.getInstance().getPalletsOnPositionMap().get(pos).get(palletTo).put(material,
                            Warehouse.getInstance().getPalletsOnPositionMap().get(pos).get(palletTo).get(material) + quantity);
                }
            }
        }
        db.persistMaterialOnPallet(palletTo, material, quantity);
    }
}
