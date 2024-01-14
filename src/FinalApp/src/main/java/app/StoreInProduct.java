package app;

import Entity.Position;
import GUI.StoreInProduct.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StoreInProduct {
    CustomerTruckNumberDataSet customerTruckNumberDataSet;
    PalletInformationDataSet palletInformationDataSet;
    HistoryRecord historyRecord;

    public StoreInProduct() {
        historyRecord = new HistoryRecord();
    }

    /***
     * Method that finds all available positions that match the requirements from forms
     * @return list of available positions
     */
    public List<String> getFreePositionsNames(){
        List<Position> freePositions = Warehouse.getInstance().getDatabaseHandler().getFreePositions();
        List<String> freePositionNames = new ArrayList<>();
        for (Position p : freePositions){
            freePositionNames.add(p.getName());
        }
        return freePositionNames;
    }

    /***
     * Method that stores in product
     */
    public void storeInProduct(){
        DatabaseHandler databaseHandler = Warehouse.getInstance().getDatabaseHandler();
        PalletInformationController palletInformationController = (PalletInformationController) Warehouse.getInstance().getController("palletInformation");
        StoreInPositionController storeInPositionController = (StoreInPositionController) Warehouse.getInstance().getController("storeInPosition");

        // stores pallet to table pallet
        databaseHandler.savePalletToDB(palletInformationController.getPNR(), palletInformationController.getWeight(),
                java.sql.Date.valueOf(LocalDate.now()), palletInformationController.getIsDamaged(), Warehouse.getInstance().getCurrentUser().getId(),
                palletInformationController.getPalletType(), storeInPositionController.getNote());

        // stores material to table material and also material and its count to table stored_on_pallet
        Map<String, Integer> materials = palletInformationController.getMaterialMap();
        for (String material : materials.keySet()){
            databaseHandler.saveMaterialToDB(material);
            databaseHandler.saveMaterialAndCountToDB(palletInformationController.getPNR(), material, materials.get(material));
        }

        // stores pallet and its position to table pallet_on_position
        String[] positions = storeInPositionController.getPosition().split("-");
        for (String position : positions){
            databaseHandler.savePalletOnPositionToDB(palletInformationController.getPNR(), position);
        }
    }

    public void saveHistoryRecord(){
        DatabaseHandler databaseHandler = Warehouse.getInstance().getDatabaseHandler();
        HistoryRecord historyRecord = Warehouse.getInstance().getStoreInInstance().getHistoryRecord();
        databaseHandler.saveHistoryRecord(historyRecord.getCustomerID(), historyRecord.getNumberOfPallets(),
                historyRecord.getTruckNumber());
    }

    public HistoryRecord getHistoryRecord() {
        return historyRecord;
    }

    public CustomerTruckNumberDataSet getCustomerTruckNumberDataSet() {
        return customerTruckNumberDataSet;
    }

    public void initializeCustomerTruckNumberDataSet(String customer, int truckNumber){
        customerTruckNumberDataSet = new CustomerTruckNumberDataSet(customer, truckNumber);
    }

    public void removeCustomerTruckDataSet(){
        customerTruckNumberDataSet = null;
    }

    public void initializePalletInformationDataSet(String PNR, boolean isDamaged, boolean isTall, Map<String, Integer> materialMap,
                                                   String palletType, Integer weight){
        palletInformationDataSet = new PalletInformationDataSet(PNR, isDamaged, isTall, materialMap, palletType, weight);
    }

    public void removePalletInformationDataSet(){
        palletInformationDataSet = null;
    }

    public PalletInformationDataSet getPalletInformationDataSet(){
        return palletInformationDataSet;
    }
}
