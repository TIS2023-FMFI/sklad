package app;

import Entity.Customer;
import Entity.Material;
import Entity.Pallet;
import Entity.Position;
import Exceptions.MaterialNotAvailable;
import javafx.collections.ObservableList;
import javafx.util.Pair;

import java.util.*;


public class OrderProduct {

    /***
     * Method that checks if the customer can order the given material in the given quantity.
     * @param customer Customer that wants to order the material.
     * @param material Material that the customer wants to order.
     * @param quantity Quantity of the material that the customer wants to order.
     * @return True if the customer has enough material stored in the warehouse, false if they don't.
     */
    public boolean canCustomerOrder(Customer customer, Material material, int quantity) {
        var dbh = Warehouse.getInstance().getDatabaseHandler();
        List<Position> positions = dbh.getPositionsReservedByCustomer(customer.getName());
        Set<Pallet> seenPalletes = new HashSet<>();
        for (Position pos : positions) {
            List<Pallet> pallets = dbh.getPalletesOnPosition(pos.getName());
            for (Pallet pallet : pallets) {
                if (seenPalletes.contains(pallet)) {
                    continue;
                }
                seenPalletes.add(pallet);
                Integer num = dbh.getMaterialQuantityOnPallet(pallet, material);
                quantity -= num;
                if (quantity <= 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /***
     * Method that returns a list of maps that contain the information about the material, quantity, position and PNR
     * of the pallets that contain material included in an order.
     * @param customer Customer that wants to order the material.
     * @param products List of pairs of materials and their quantities that the customer wants to order.
     * @return List of maps that contain the information about the material, quantity, position and PNR
     * of the pallets that contain the material.
     * @throws MaterialNotAvailable Exception that is thrown when the customer doesn't have enough material stored in
     * the warehouse.
     */
    public List<Map<String, String>> setOrderTable(Customer customer, List<Pair<Material, Integer>> products) throws MaterialNotAvailable {
        List<Map<String, String>> res = new ArrayList<>();
        var dbh = Warehouse.getInstance().getDatabaseHandler();
        List<Position> customersPositions = dbh.getPositionsReservedByCustomer(customer.getName());
        List<Pallet> seenPalletes = new ArrayList<>();
        for (Pair<Material, Integer> product : products) {
            Material mat = product.getKey();
            int quantity = product.getValue();
            boolean enough = false;
            for (Position position : customersPositions) {
                List<Pallet> pals = dbh.getPalletesOnPosition(position.getName());
                for (Pallet pal : pals) {
                    if (seenPalletes.contains(pal)) {
                        continue;
                    }
                    seenPalletes.add(pal);
                    int num = dbh.getMaterialQuantityOnPallet(pal, mat);
                    if (num >= quantity) {
                        String poss = getPositionsForPalletString(pal);
                        enough = true;
                        Map<String, String> row = Map.of(
                                "Materiál", mat.getName(),
                                "Počet", String.valueOf(quantity),
                                "Pozícia", poss,
                                "PNR", pal.getPnr()
                        );
                        res.add(row);
                        break;
                    } else if (num > 0) {
                        String poss = getPositionsForPalletString(pal);
                        Map<String, String> row = Map.of(
                                "Materiál", mat.getName(),
                                "Počet", String.valueOf(num),
                                "Pozícia", poss,
                                "PNR", pal.getPnr()
                        );
                        res.add(row);
                        quantity -= num;
                    }
                }
                if (enough) {
                    break;
                }
            }
            if (!enough) {
                throw new MaterialNotAvailable(mat.getName());
            }
        }
        return res;
    }

    /***
     * Method that deletes the ordered material from database and from memory.
     * @param items List of maps that contain the information about the material, quantity,
     *              position and PNR of meterials that were ordered.
     */
    public void removeOrderedItems(ObservableList<Map<String, String>> items) {
        var dbh = Warehouse.getInstance().getDatabaseHandler();
        for (Map<String, String> item : items) {
            //Position position = dbh.getPosition(item.get("Pozícia"));
            Pallet pnr = dbh.getPallet(item.get("PNR"));
            List<Position> positions = getPositionsForPallet(pnr);

            Material material = null;
            try {
                material = dbh.getMaterial(item.get("Materiál"));
            } catch (MaterialNotAvailable e) {
                System.out.println(e.getMessage());
            }
            int quantity = Integer.parseInt(item.get("Počet"));


            boolean removePallet = false;
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
}