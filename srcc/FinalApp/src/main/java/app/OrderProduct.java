package app;

import Entity.Customer;
import Entity.Material;
import Entity.Pallet;
import Entity.Position;

import java.util.List;

public class OrderProduct {
    public OrderProduct() {}

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
        for (Position pos:positions) {
            List<String> pallets = dbh.getPalletesOnPosition(pos.getName());
            for (String palletName:pallets) {
                Pallet pallet = dbh.getPallet(palletName);
                Integer num = dbh.getMaterialQuantityOnPallet(pallet, material);
                quantity -= num;
                if (quantity <= 0) {
                    return true;
                }
            }
        }
        return true;
    }
}
