package app;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;


public class CreateNewCustomerTest {
    // v databaze v tabulke Customers sa momentalne nachadzaju: BMW, ZSSK, Volvo
    // po skusteni testov pribudnu v DB Kia, FMFI, Unik
  /*  @Test
    public void dupliciteNames(){
        CustomersHandler customersHandler = new CustomersHandler();
        assertFalse(customersHandler.saveCustomer("BMW"));
        assertFalse(customersHandler.saveCustomer("Volvo"));

    }
    @Test
    public void addNewCustomer(){
        CustomersHandler customersHandler = new CustomersHandler();
        String name = "Kia";
        assertTrue(customersHandler.saveCustomer(name));
        assertFalse(customersHandler.saveCustomer(name));
    }

    @Test
    public void addMultipleCustomers(){
        CustomersHandler customersHandler = new CustomersHandler();
        String name = "FMFI";
        assertTrue(customersHandler.saveCustomer(name));
        name = "Unik";
        assertTrue(customersHandler.saveCustomer(name));
    }*/
}
