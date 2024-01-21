package app;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.ObservableList;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class FileExporter {
    /***
     * Exports data to excel file
     * @param items data to export
     * @param filename name of the file
     * @param sheetname name of the sheet in the file
     * @param columns columns to export
     */
    public void exportExcel(ObservableList<Map<String, String>> items, String filename, String sheetname, List<String> columns) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet spreadsheet = workbook.createSheet(sheetname);

        Map<String, String[]> data = new TreeMap<>();
        String[] columnsArr = columns.toArray(columns.toArray(new String[0]));
        //var columns = new Object[] { "Materiál", "Počet", "Pozícia", "PNR" };
        data.put("1", columnsArr);
        for (int i = 0; i < items.size(); i++) {
            Map<String, String> row = items.get(i);
            List<String> roww = new ArrayList<>();
            for (String colName:columns) {
                 roww.add(row.get(colName));
            }
            data.put(Integer.toString(i+2), roww.toArray(new String[0]));
        }
        Set<String> keyid = data.keySet();
        int rowid = 0;
        XSSFRow row;
        for (String key : keyid) {
            row = spreadsheet.createRow(rowid++);
            String[] objectArr = data.get(key);
            int cellid = 0;

            for (String obj : objectArr) {
                Cell cell = row.createCell(cellid++);
                cell.setCellValue(obj);
            }
        }
        try {
            FileOutputStream out = new FileOutputStream(
                    new File("exports/"+filename+".xlsx"));
            workbook.write(out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportInvoicingPDF(String customer, String dateFrom, String dateTo, String price){
        Document doc = new Document();
        try
        {
            //generate a PDF at the specified location
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("exports/test.pdf"));
            doc.open();

            float twoCol = 285f;
            float twoCol150 = twoCol+150f;
            float twoColWidth[] = {twoCol, twoCol150};

            PdfPTable table = new PdfPTable(8);
            for(int aw = 0; aw < 16; aw++){
                table.addCell("hi");
            }
            doc.add(table);

            //doc.add(new Paragraph("If you're offered a seat on a rocket ship, don't ask what seat! Just get on."));


            doc.close();
            writer.close();
        }
        catch (DocumentException | FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }
}
