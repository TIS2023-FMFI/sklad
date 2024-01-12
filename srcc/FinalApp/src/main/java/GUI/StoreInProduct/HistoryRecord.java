package GUI.StoreInProduct;

public class HistoryRecord {
    private int customerID;
    private int numberOfPallets;
    private int truckNumber;

    public HistoryRecord() {
        numberOfPallets = 0;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }
    public void addPallet() {
        numberOfPallets++;
    }
    public void setTruckNumber(int truckNumber) {
        this.truckNumber = truckNumber;
    }
    public int getCustomerID() {
        return customerID;
    }
    public int getNumberOfPallets() {
        return numberOfPallets;
    }
    public int getTruckNumber() {
        return truckNumber;
    }
}

