package app;

public class CustomersHandler {

     public boolean saveCustomer(String name){
         return Warehouse.getInstance().getDatabaseHandler().saveCustomer(name);
     }
}
