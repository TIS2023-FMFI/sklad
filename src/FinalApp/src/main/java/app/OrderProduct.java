package app;

import Entity.Customer;
import Entity.Material;
import Entity.Pallet;
import Entity.Position;
import Exceptions.MaterialNotAvailable;
import jakarta.persistence.criteria.CriteriaBuilder;
import javafx.collections.ObservableList;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.util.Pair;

import java.util.*;


public class OrderProduct {

    /***
     * Method that deletes the ordered material from database and from memory.
     * @param items List of maps that contain the information about the material, quantity,
     *              position and PNR of meterials that were ordered.
     */
    public void removeOrderedItems(ObservableList<Map<String, String>> items) {
        var dbh = Warehouse.getInstance().getDatabaseHandler();
        for (Map<String, String> item : items) {
            Pallet pnr = dbh.getPallet(item.get("Paleta"));
            List<Position> positions = getPositionsForPallet(pnr);

            Material material = null;
            try {
                material = dbh.getMaterial(item.get("Materiál"));
            } catch (MaterialNotAvailable e) {
                System.out.println(e.getMessage());
            }
            int quantity = Integer.parseInt(item.get("Počet"));


            boolean removePallet = false;
            System.out.println("Pozicie: " + positions);
            int numOnPos = Warehouse.getInstance().getPalletsOnPositionMap().get(positions.get(0)).get(pnr).get(material);
            if (numOnPos == quantity) {
                for (Position position : positions) {
                    try {
                        Warehouse.getInstance().getPalletsOnPositionMap().get(position).get(pnr).remove(material);
                    }catch (NullPointerException e) {
                        System.out.println("Nepodarilo sa odstranit material z pozicie");
                    }
                    if (Warehouse.getInstance().getPalletsOnPositionMap().get(position).get(pnr).isEmpty()) {
                        Warehouse.getInstance().getPalletsOnPositionMap().get(position).remove(pnr);
                        removePallet = true;
                    }
                }

            } else {
                int newQuantity = numOnPos - quantity;
                for (Position position : positions) {
                    Warehouse.getInstance().getPalletsOnPositionMap().get(position).get(pnr).put(material, newQuantity);
                }
            }
            dbh.removeItem(positions, pnr, material, quantity, removePallet);

        }
    }

    private String getPositionsForPalletString(Pallet pallet) {
        List<Position> res = new ArrayList<>();
        for (Map.Entry<Position, Map<Pallet, Map<Material, Integer>>> entry : Warehouse.getInstance().getPalletsOnPositionMap().entrySet()) {
            if (entry.getValue().containsKey(pallet)) {
                res.add(entry.getKey());
            }
        }
        StringBuilder sb = new StringBuilder();
        for (Position pos : res) {
            sb.append(pos.getName()).append("-");
        }
        return sb.substring(0, sb.length() - 1);
    }

    private List<Position> getPositionsForPallet(Pallet pallet) {
        List<Position> res = new ArrayList<>();
        for (Map.Entry<Position, Map<Pallet, Map<Material, Integer>>> entry : Warehouse.getInstance().getPalletsOnPositionMap().entrySet()) {
            if (entry.getValue().containsKey(pallet)) {
                res.add(entry.getKey());
            }
        }
        return res;
    }

    /***
     * Method that sets the table for the customer to choose the material that he wants to order.
     * @param customer Customer that wants to order the material.
     * @return List of maps that contain the information about the material, quantity,
     *              position and PNR of meterials that were ordered.
     */
    public List<Map<String, Object>> setMaterialChoiceTable(Customer customer, String materialName) {
        DatabaseHandler dbh = Warehouse.getInstance().getDatabaseHandler();
        List<Pallet> palletes = dbh.getPalletesReservedByCustomerWithMaterial(customer.getId(),materialName);
        List<Map<String, Object>> res = new ArrayList<>();
        for (Pallet p : palletes) {
            Map<String, Object> row = new HashMap<>();
            row.put("pallet", p.getPnr());
            row.put("position", getPositionsForPalletString(p));
            Integer maxnum = 0;
            try {
                maxnum = dbh.getMaterialQuantityOnPallet(p, dbh.getMaterial(materialName));
            } catch (MaterialNotAvailable e) {
                e.printStackTrace();
            }
            row.put("maxNumber", maxnum);
            row.put("date", p.getDateIncome().toString());
            Spinner<Integer> spiner = new Spinner<>();
            SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, maxnum);
            valueFactory.setValue(0);
            spiner.setValueFactory(valueFactory);
            row.put("quantity", spiner);
            res.add(row);
        }

        return res;
    }
}