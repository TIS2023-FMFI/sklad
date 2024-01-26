package app;

import Entity.Material;
import Entity.Pallet;
import Entity.Position;
import javafx.scene.chart.XYChart;
import javafx.util.Pair;

import java.sql.Date;
import java.util.*;

public class Statistics {
    /***
     * This method takes data from database and fills the chart with them.
     * @param dateFrom Date from which the data should be taken.
     * @param dateTo Date to which the data should be taken.
     * @param customerName Name of the customer whose data should be taken.
     * @return BarChart with data.
     */
    public List<XYChart.Series<String,Number>> setBarChart(Date dateFrom, Date dateTo, String customerName) {
        Map<Date, Pair<Integer,Integer>> data = Warehouse.getInstance().getDatabaseHandler()
                                                .getStatistics(dateFrom, dateTo, customerName);

        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("Import");
        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series2.setName("Export");

        List<Map.Entry<Date, Pair<Integer, Integer>>> stats = new ArrayList<>(data.entrySet());
        stats.sort(new StatsDateComparator());


        for (Map.Entry<Date, Pair<Integer,Integer>> entry : stats) {
            series1.getData().add(new XYChart.Data<>(entry.getKey().toString(), entry.getValue().getKey()));
            series2.getData().add(new XYChart.Data<>(entry.getKey().toString(), entry.getValue().getValue()));
        }

        return Arrays.asList(
                series1,
                series2
        );
    }

    /***
     * This method takes data from database and fills in the table used for inventory check.
     * @return List of maps with data.
     */
    public List<Map<String, String>> setInventoryTable(){
        Set<Pallet> usedPallets = new HashSet<>();
        List<Map<String,String>> res = new ArrayList<>();
        //var data = Warehouse.getInstance().getWarehouseData();
        var data = Warehouse.getInstance().getPalletsOnPositionMap();
        //var dbh = Warehouse.getInstance().getDatabaseHandler();
        //Map<Position, Map<Pallet, Map<Material, Integer>>> palletsOnPosition;
        for (Position position : data.keySet()) {
            var pallets = data.get(position);
            for (Pallet pallet : pallets.keySet()){
                if (usedPallets.contains(pallet)) {
                    continue;
                }
                usedPallets.add(pallet);
                var materials = pallets.get(pallet);
                for (Material material : materials.keySet()) {
                    res.add(Map.of(
                            "Pozícia", position.getName(),
                            "PNR", pallet.getPnr(),
                            "Materiál", material.getName(),
                            "Počet", String.valueOf(materials.get(material))
                    ));
                }

            }
        }
        return res;
    }

    public class StatsDateComparator implements Comparator<Map.Entry<Date, Pair<Integer,Integer>>> {
        @Override
        public int compare(Map.Entry<Date, Pair<Integer,Integer>> entry1,
                           Map.Entry<Date, Pair<Integer,Integer>> entry2) {
            Date d1 = entry1.getKey();
            Date d2 = entry2.getKey();

            return d1.compareTo(d2);
        }
    }

}
