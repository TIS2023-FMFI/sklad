package app;

import Entity.Customer;
import Entity.Material;
import Entity.Pallet;
import Entity.Position;
import Exceptions.MaterialNotAvailable;
import GUI.StoreInProduct.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StoreInProduct {
    private CustomerTruckNumberDataSet customerTruckNumberDataSet;
    private PalletInformationDataSet palletInformationDataSet;
    private HistoryRecord historyRecord;

    /***
     * Constructor for StoreInProduct. It sets the historyRecord to a new instance of HistoryRecord.
     */
    public StoreInProduct() {
        historyRecord = new HistoryRecord();
    }

    /***
     * Method that finds all available positions that match the requirements from forms
     * @return list of available positions
     */
    public List<String> getFreePositionsNames(){
        Warehouse warehouse = Warehouse.getInstance();
        CustomerTruckNumberController customerTruckNumberController = (CustomerTruckNumberController) warehouse.getController("customerTruckNumber");
        PalletInformationController palletInformationController = (PalletInformationController) warehouse.getController("palletInformation");

        Customer customer = warehouse.getDatabaseHandler().getCustomer(customerTruckNumberController.getCustomer());
        double weight = palletInformationController.getWeight();
        boolean isTall = palletInformationController.getIsTall();
        int numberOfPositions = palletInformationController.getNumberOfPositions();

        List<List<Position>> freePositions = warehouse.getDatabaseHandler().getFreePositions(customer, weight, isTall, numberOfPositions);

        return freePositions.stream().map(positions -> positions.stream().map(Position::getName)
                .collect(Collectors.joining("-"))).toList();
    }

    /***
     * Method that stores in product
     */
    public void storeInProduct() {
        Warehouse warehouse = Warehouse.getInstance();
        DatabaseHandler databaseHandler = warehouse.getDatabaseHandler();
        PalletInformationController palletInformationController = (PalletInformationController) warehouse.getController("palletInformation");
        StoreInPositionController storeInPositionController = (StoreInPositionController) warehouse.getController("storeInPosition");

        // creates a pallet for saving into memory
        Pallet pallet = new Pallet();
        pallet.setPnr(palletInformationController.getPNR());
        pallet.setWeight(palletInformationController.getWeight());
        pallet.setNumberOfPositions(palletInformationController.getNumberOfPositions());
        pallet.setDateIncome(java.sql.Date.valueOf(LocalDate.now()));
        pallet.setDamaged(palletInformationController.getIsDamaged());
        pallet.setIdUser(warehouse.getCurrentUser().getId());
        pallet.setType(palletInformationController.getPalletType());
        pallet.setNote(storeInPositionController.getNote());

        // stores pallet to table pallet
        databaseHandler.savePalletToDB(pallet);

        // stores material to table material and also material and its count to table stored_on_pallet
        // creates material Map for saving into memory
        Map<String, Integer> materials = palletInformationController.getMaterialMap();
        Map<Material, Integer> materialMap = new HashMap<>();

        for (String material : materials.keySet()){
            databaseHandler.saveMaterialToDB(material);
            databaseHandler.saveMaterialAndCountToDB(palletInformationController.getPNR(), material, materials.get(material));

            try {
                materialMap.put(warehouse.getDatabaseHandler().getMaterial(material), materials.get(material));
            } catch (MaterialNotAvailable e) {
                throw new RuntimeException(e);
            }
        }

        // stores pallet and its position to table pallet_on_position
        // saves the pallet to the palletsOnPosition map
        String[] positions = storeInPositionController.getPosition().split("-");
        for (String position : positions){
            databaseHandler.savePalletOnPositionToDB(palletInformationController.getPNR(), position);
            warehouse.getPalletsOnPositionMap().get(warehouse.getDatabaseHandler().getPosition(position)).put(pallet, materialMap);
        }
    }

    /***
     * Method that saves a history record
     */
    public void saveHistoryRecord(){
        DatabaseHandler databaseHandler = Warehouse.getInstance().getDatabaseHandler();
        HistoryRecord historyRecord = Warehouse.getInstance().getStoreInInstance().getHistoryRecord();
        databaseHandler.saveHistoryRecord(historyRecord.getCustomerID(), historyRecord.getNumberOfPallets(),
                historyRecord.getTruckNumber(), true);
    }

    /***
     * Method that returns the history record
     */
    public HistoryRecord getHistoryRecord() {
        return historyRecord;
    }


    /***
     * Method that initializes the customer truck number data set
     * @param customer customer which truck has arrived
     * @param truckNumber number of the truck today
     */
    public void initializeCustomerTruckNumberDataSet(String customer, int truckNumber){
        customerTruckNumberDataSet = new CustomerTruckNumberDataSet(customer, truckNumber);
    }

    /***
     * Method that returns the customer truck number data set
     * @return customer truck number data set
     */
    public CustomerTruckNumberDataSet getCustomerTruckNumberDataSet() {
        return customerTruckNumberDataSet;
    }

    /***
     * Method that removes the customer truck number data set
     */
    public void removeCustomerTruckDataSet(){
        customerTruckNumberDataSet = null;
    }

    /***
     * Method that initializes the pallet information data set
     * @param PNR PNR of the pallet
     * @param weight weight of the pallet
     * @param isDamaged if the pallet is damaged
     * @param isTall if the pallet is tall
     * @param materialMap map of materials and their count
     * @param palletType type of the pallet
     * @param numberOfPositions number of positions
     */
    public void initializePalletInformationDataSet(String PNR, double weight, boolean isDamaged, boolean isTall, Map<String, Integer> materialMap,
                                                   String palletType, int numberOfPositions){
        palletInformationDataSet = new PalletInformationDataSet(PNR, weight, isDamaged, isTall, materialMap, palletType, numberOfPositions);
    }

    /***
     * Method that returns the pallet information data set
     * @return pallet information data set
     */
    public PalletInformationDataSet getPalletInformationDataSet(){
        return palletInformationDataSet;
    }

    /***
     * Method that removes the pallet information data set
     */
    public void removePalletInformationDataSet(){
        palletInformationDataSet = null;
    }
}
