package app;

import Entity.Customer;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class FileExporter {
    private final Font customFont = FontFactory.getFont("arialuni.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12);
    private final Font customFontSmall = FontFactory.getFont("arialuni.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 10);
    private final Font customFontBig = FontFactory.getFont("arialuni.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 15, Font.BOLD);
    private final Font customFontBold = FontFactory.getFont("arialuni.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12, Font.BOLD);
    private final Font customFontSmallBold = FontFactory.getFont("arialuni.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 9, Font.BOLD);


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
            FileChooser fileChooser = new FileChooser();

            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel files (*.xlsx)", "*.xlsx");
            fileChooser.getExtensionFilters().add(extFilter);

            File file = fileChooser.showSaveDialog(Warehouse.getStage());
            if (file == null) {
                return;
            }

            FileOutputStream out = new FileOutputStream(file.getAbsolutePath());
            workbook.write(out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * Exports data concerning invoicing to pdf file
     * @param customer Name of a customer who is being invoiced
     * @param dateFrom Date from which the invoicing is being done
     * @param dateTo Date to which the invoicing is being done
     * @param price Price of the invoicing
     * @param totalReservations Total number of reserved warehouse spaces per day in the invoicing period
     */
    public void exportInvoicingPDF(String customer, String dateFrom, String dateTo, String price, int totalReservations){
        try {
            FileChooser fileChooser = new FileChooser();

            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF (*.pdf)", "*.pdf");
            fileChooser.getExtensionFilters().add(extFilter);

            File file = fileChooser.showSaveDialog(Warehouse.getStage());
            if (file == null) {
                return;
            }

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file.getAbsolutePath()));
            document.open();

            DatabaseHandler dbh = Warehouse.getInstance().getDatabaseHandler();
            Customer cust = dbh.getCustomer(customer);

            int intPrice = Integer.parseInt(price.substring(0,price.indexOf(" €")));
            addInvoiceContent(document, cust, dateFrom, dateTo, intPrice, totalReservations);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addInvoiceContent(Document document, Customer customer, String dateFrom,
                                        String dateTo, int price, int reservations) throws DocumentException {
        // Add a title to the document

        Paragraph title = new Paragraph("Faktúra", customFontBig);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        // Add provider information
        document.add(new Paragraph("Informácie o sprostredkovateľovi:", customFontBold));
        PdfPTable tableProvider = new PdfPTable(2);
        tableProvider.setWidthPercentage(100);

        Paragraph providerInfo = new Paragraph(
                """
                      Názov: Gefco Slovakia s.r.o.
                               Distribution Center, logistická hala DC1
                      Adresa: SNP 811/168
                      Mesto: 013 24 Strečno
                      """, customFontSmall);
        providerInfo.setSpacingAfter(30);
        PdfPCell providerCell = new PdfPCell(providerInfo);

        //customerCell.setVerticalAlignment(Element.ALIGN_TOP);
        providerCell.setBorder(Rectangle.NO_BORDER);
        tableProvider.addCell(providerCell);


        // Add the image to the second cell
        PdfPCell imageCell = new PdfPCell();
        try {
            Image image = Image.getInstance("src/main/resources/CEVA_Logistics_Logo.png");
            image.scaleToFit(200, 200); // Adjust the image size as needed
            imageCell.addElement(image);
            imageCell.setVerticalAlignment(Element.ALIGN_TOP);
            imageCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            imageCell.setBorder(Rectangle.NO_BORDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tableProvider.addCell(imageCell);

        document.add(tableProvider);

        document.add(new Paragraph("Informácie o zákazníkovi:                                      " +
                "Informácie o faktúre:",
                customFontBold));

        PdfPTable tableCustomer = new PdfPTable(2);
        tableCustomer.setWidthPercentage(100);

        Paragraph customerInfo = new Paragraph("Názov: " + customer.getName(), customFontSmall);
        customerInfo.add("\nAdresa: " + customer.getAddress());
        customerInfo.add("\nMesto:" + customer.getPostalCode() + " " + customer.getCity() + "\n");
        PdfPCell customerCell = new PdfPCell(customerInfo);
        customerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        customerCell.setBorder(Rectangle.NO_BORDER);
        tableCustomer.addCell(customerCell);


        Paragraph invoicingInfo = new Paragraph("Číslo faktúry: \n", customFontSmall);
        invoicingInfo.add("Dátum vystavenia: " + LocalDate.now().format(
                DateTimeFormatter.ofPattern("dd.MM.yyyy")));

        PdfPCell invoiceCell = new PdfPCell(invoicingInfo);
        invoiceCell.setBorder(Rectangle.NO_BORDER);
        tableCustomer.addCell(invoiceCell);

        document.add(tableCustomer);


        // Step 4: Add items to the invoice (example)
        document.add(Chunk.NEWLINE);
        document.add(new Paragraph("Produkty faktúry:", customFontBold));
        document.add(Chunk.NEWLINE);
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.addCell(new Paragraph("Položka", customFontBold));
        table.addCell(new Paragraph("Počet", customFontBold));
        table.addCell(new Paragraph("Cena za kus", customFontBold));
        table.addCell(new Paragraph("Pozícia v sklade", customFont));
        table.addCell(new Paragraph(String.valueOf(reservations), customFont));
        table.addCell(new Paragraph(price/reservations + " €", customFont));
        document.add(table);

        document.add(Chunk.NEWLINE);
        document.add(new Paragraph("Celková suma: " + price + " €", customFontBig));

        document.add(Chunk.NEWLINE);
        document.add(new Paragraph("*Cena je vypočítaná podľa počtu rezervovnaých miest v sklade za každý" +
                "deň intervalu: " + changeDateFormat(dateFrom) + "-" + changeDateFormat(dateTo) + ".", customFontSmall));
    }

    private String changeDateFormat(String date){
        String year = date.substring(0, date.indexOf("-"));
        date = date.substring(date.indexOf("-")+1);
        String month = date.substring(0, date.indexOf("-"));
        String day = date.substring(date.indexOf("-")+1);
        return day + "." + month + "." + year;
    }

    /**
     * Exports the order items and their respective positions to PDF
     * @param items items to be exported
     * @param customer customer to be exported
     */
    public void exportOrderPDF(ObservableList<Map<String, String>> items, Customer customer) {
        try {
            FileChooser fileChooser = new FileChooser();

            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF (*.pdf)", "*.pdf");
            fileChooser.getExtensionFilters().add(extFilter);

            File file = fileChooser.showSaveDialog(Warehouse.getStage());
            if (file == null) {
                return;
            }


            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file.getAbsolutePath()));
            document.open();

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            PdfPCell c = new PdfPCell(new Paragraph("Dodací list č:", customFontBold));
            c.setBorder(Rectangle.NO_BORDER);
            table.addCell(c);
            PdfPCell c1 = new PdfPCell(new Paragraph("", customFont));
            c1.setBorder(Rectangle.NO_BORDER);
            table.addCell(c1);
            PdfPCell c2 = new PdfPCell(new Paragraph("Dátum:", customFontBold));
            c2.setBorder(Rectangle.NO_BORDER);
            table.addCell(c2);
            PdfPCell c3 = new PdfPCell(new Paragraph(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), customFont));
            c3.setBorder(Rectangle.NO_BORDER);
            table.addCell(c3);

            document.add(table);


            PdfPTable table2 = new PdfPTable(2);
            table2.setWidthPercentage(100);


            PdfPCell cellFrom = new PdfPCell(new Paragraph("""
                   Sprostredkovateľ:
                    
                      Gefco Slovakia s.r.o.
                      Distribution Center, logistická hala DC1
                      SNP 811/168
                      013 24 Strečno
                    """,
                    customFont));
            table2.addCell(cellFrom);


            PdfPCell cellTo = new PdfPCell(new Paragraph("""
                    Odoberateľ:
                    
                   """ + "    " + customer.getName() + """
                        
                    """ + "    " + customer.getAddress() + """
                    
                    """ + "    " + customer.getCity() + """
                    
                    """ + "    " + customer.getPostalCode() + """
                    """,
                    customFont));
            table2.addCell(cellTo);

            document.add(table2);



            PdfPTable contentTable = new PdfPTable(6);
            contentTable.setWidthPercentage(100);
            PdfPCell cell = new PdfPCell(new Paragraph("Číslo:", customFontBold));
            contentTable.addCell(cell);

            PdfPCell cell2 = new PdfPCell(new Paragraph("Pozícia:", customFontBold));
            contentTable.addCell(cell2);

            PdfPCell cell3 = new PdfPCell(new Paragraph("Paleta:", customFontBold));
            contentTable.addCell(cell3);

            PdfPCell cell4 = new PdfPCell(new Paragraph("Materiál:", customFontBold));
            contentTable.addCell(cell4);

            PdfPCell cell5 = new PdfPCell(new Paragraph("Množstvo:", customFontBold));
            contentTable.addCell(cell5);

            PdfPCell cell1 = new PdfPCell(new Paragraph("Poznámka:", customFontBold));
            contentTable.addCell(cell1);

            int num = 1;

            for (Map<String, String> item:items) {
                contentTable.addCell(new Paragraph(String.valueOf(num), customFont));
                contentTable.addCell(new Paragraph(item.get("Pozícia"), customFont));
                contentTable.addCell(new Paragraph(item.get("PNR"), customFont));
                contentTable.addCell(new Paragraph(item.get("Materiál"), customFont));
                contentTable.addCell(new Paragraph(item.get("Počet"), customFont));
                contentTable.addCell(new Paragraph("", customFont));
                num++;
            }
            document.add(contentTable);


            PdfPTable signTable = new PdfPTable(3);
            signTable.setWidthPercentage(100);

            PdfPCell gave = new PdfPCell(new Paragraph("Odovzdal:", customFontSmallBold));
            signTable.addCell(gave);

            PdfPCell transported = new PdfPCell(new Paragraph("Prepravil:", customFontSmallBold));
            signTable.addCell(transported);

            PdfPCell accepted = new PdfPCell(new Paragraph("Prevzal:", customFontSmallBold));
            signTable.addCell(accepted);


            PdfPCell gaveSign = new PdfPCell(new Paragraph("""
                               
                               
                               
                               
                               (meno,peciatka,podpis) 
                               """,
                    customFontSmall));
            signTable.addCell(gaveSign);

            PdfPCell transportedSign = new PdfPCell(new Paragraph("""
                               
                               
                               
                               
                               (meno,peciatka,podpis) 
                               """,
                    customFontSmall));
            signTable.addCell(transportedSign);

            PdfPCell acceptedSign = new PdfPCell(new Paragraph("""
                               
                               
                               
                               
                               (meno,peciatka,podpis) 
                               """,
                    customFontSmall));
            signTable.addCell(acceptedSign);

            document.add(signTable);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
