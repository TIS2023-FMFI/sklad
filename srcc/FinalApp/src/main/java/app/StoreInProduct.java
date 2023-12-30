package app;

import Entity.Position;
import GUI.StoreInProduct.CustomerTruckNumberController;
import GUI.StoreInProduct.PalletInformationController;
import GUI.StoreInProduct.StoreInPositionController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StoreInProduct {

    public StoreInProduct() {}

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
        CustomerTruckNumberController customerTruckNumberController = (CustomerTruckNumberController) Warehouse.getInstance().controllers.get("customerTruckNumber");
        PalletInformationController palletInformationController = (PalletInformationController) Warehouse.getInstance().controllers.get("palletInformation");
        StoreInPositionController storeInPositionController = (StoreInPositionController) Warehouse.getInstance().controllers.get("storeInPosition");

        // stores pallet to table pallet
        databaseHandler.savePalletToDB(palletInformationController.getPNR(), palletInformationController.getWeight(),
                java.sql.Date.valueOf(LocalDate.now()), palletInformationController.getIsDamaged(), Warehouse.getInstance().getCurrentUser().getId(),
                palletInformationController.getPalletType(), storeInPositionController.getNote());

        /*
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
         */
    }
}
