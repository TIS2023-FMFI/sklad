package com.example.warehouse;

import java.util.Date;
import java.util.Map;

public class Pallet {
    private int PNR;
    private Position position;
    private Map<Material, Integer> materials;
    private Date storedInDate;
    private User responsibleEmployee;
    private String palletType;
    private boolean damaged;
    private double weight;

    private String note;

    public Pallet(){}

    public void setPNR(int PNR) {
        this.PNR = PNR;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setMaterials(Map<Material, Integer> materials) {
        this.materials = materials;
    }

    public void setStoredInDate(Date storedInDate) {
        this.storedInDate = storedInDate;
    }

    public void setResponsibleEmployee(User responsibleEmployee) {
        this.responsibleEmployee = responsibleEmployee;
    }

    public void setPalletType(String palletType) {
        this.palletType = palletType;
    }

    public void setDamaged(boolean damaged) {
        this.damaged = damaged;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void changePNRNumber(String newNumber){

    }
    public void changeResponsibleEmployee(User newUser){

    }

    public boolean containsProduct(Material material){
        return true;
    }

    public void addProduct(Material material, int count){}

    public void removeProduct(Material material, int count){}

    public Map<Material, Integer> getAllProducts(){
        return null;
    }
}
