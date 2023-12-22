package app;

import javafx.collections.ObservableList;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class FileExporter {
    public void exportExcel(ObservableList<Map<String, Object>> items, String sheetname) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet spreadsheet = workbook.createSheet(sheetname);

        Map<String, Object[]> data = new TreeMap<>();
        var columns = items.get(0).keySet();
        //var columns = new Object[] { "Materiál", "Počet", "Pozícia", "PNR" };
        data.put("1", columns.toArray());
        for (int i = 0; i < items.size(); i++) {
            Map<String, Object> row = items.get(i);
            data.put(Integer.toString(i+2),
                    new Object[] { row.get("Materiál"),
                            row.get("Počet"),
                            row.get("Pozícia"),
                            row.get("PNR") });
        }
        Set<String> keyid = data.keySet();

        int rowid = 0;
        XSSFRow row;
        for (String key : keyid) {
            row = spreadsheet.createRow(rowid++);
            Object[] objectArr = data.get(key);
            int cellid = 0;

            for (Object obj : objectArr) {
                Cell cell = row.createCell(cellid++);
                cell.setCellValue((String)obj);
            }
        }
        try {
            FileOutputStream out = new FileOutputStream(
                    new File("exports/Order.xlsx"));
            workbook.write(out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
