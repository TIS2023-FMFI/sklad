package GUI.StoreInProduct;

import java.util.Map;

public record PalletInformationDataSet(String PNR, double weight, boolean isDamaged, boolean isTall, Map<String, Integer> materialMap,
                                       String palletType, int numberOfPositions) {
}
