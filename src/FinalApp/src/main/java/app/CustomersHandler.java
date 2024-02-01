package app;

import Entity.Customer;

public class CustomersHandler {
    /**
     * Checks if customer with given name exists in database.
     * @param name name of customer
     * @return true if customer exists, false otherwise
     */
    public boolean checkCustomerName(String name){
        return Warehouse.getInstance().getDatabaseHandler().checkCustomerExist(name);
    }

    /***
     * Saves customer to database.
     * @param customer customer to save
     * @return true if customer was saved, false otherwise
     */
     public boolean saveCustomer(Customer customer){
         return Warehouse.getInstance().getDatabaseHandler().saveCustomer(customer);
     }
}
