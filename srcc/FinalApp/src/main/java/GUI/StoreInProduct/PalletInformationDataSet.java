package GUI.StoreInProduct;

import java.util.Map;

public record PalletInformationDataSet(String PNR, boolean isDamaged, boolean isTall, Map<String, Integer> materialMap,
                                       String palletType, Integer weight) {
}
