package app;

import Entity.Customer;

public class CustomersHandler {

    public boolean checkCustomerName(String name){
        return Warehouse.getInstance().getDatabaseHandler().checkCustomerExist(name);
    }
     public boolean saveCustomer(Customer customer){
         return Warehouse.getInstance().getDatabaseHandler().saveCustomer(customer);
     }
}
