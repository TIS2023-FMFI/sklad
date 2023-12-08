package app;

import Entity.Customer;
import Entity.Material;
import Entity.Position;
import javafx.util.Pair;

import java.util.List;

public class StoreInProduct {
    Customer customer;
    int truckNumber;  //cislo tocky
    List<Pair<Material, Integer>> addMaterials;
    boolean requiresTallPosition;
    boolean isDamaged;
    int weight;
    String note;

    Customer setCustomer(String name){return new Customer();}
    public boolean setTruckNumber(int truckNumber) {return false;}
    Material checkMaterial(String name){return new Material();}
    Position findBestPosition(){return new Position();}

    /**pouzijeme na generovanie moznosti, z ktorych si bude moct zakaznik vybrat ak nebude spokojny s navrhovanou poziciou**/
    List<Position> allEmptyPositions(){return null;}
    /** ak chce zakaznik pokracovat v zaskladnovani, tak vymaze nepotrebne udaje z pamati**/
    void resetData(){}

}
