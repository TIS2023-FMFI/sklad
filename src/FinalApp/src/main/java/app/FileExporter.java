package app;

import Entity.Customer;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

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

    public void exportInvoicingPDF(String customer, String dateFrom, String dateTo, String price, int totalReservations){
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("exports/Invoice.pdf"));
            document.open();

            DatabaseHandler dbh = Warehouse.getInstance().getDatabaseHandler();
            Customer cust = dbh.getCustomer(customer);

            int intPrice = Integer.parseInt(price.substring(0,price.indexOf(" €")));
            addInvoiceContent(document, cust, dateFrom, dateTo, intPrice, totalReservations);

            document.close();
            System.out.println("Invoice generated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addInvoiceContent(Document document, Customer customer, String dateFrom,
                                        String dateTo, int price, int reservations) throws DocumentException {
        // Add a title to the document
        Paragraph title = new Paragraph("Faktúra", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18));
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        // Add provider information
        document.add(new Paragraph("Informácie o sprostredkovateľovi:",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
        PdfPTable tableProvider = new PdfPTable(2);
        tableProvider.setWidthPercentage(100);

        Paragraph providerInfo = new Paragraph(
                """
                      Názov: CEVA Logistics
                      Adresa: 123 Hlavná ulica
                      Mesto: Strecno
                      """, FontFactory.getFont(FontFactory.HELVETICA, 12));
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

        document.add(new Paragraph("Informácie o zákazníkovi:                                  " +
                "Informácie o faktúre:",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));

        PdfPTable tableCustomer = new PdfPTable(2);
        tableCustomer.setWidthPercentage(100);

        Paragraph customerInfo = new Paragraph("Názov: " + customer.getName());
        customerInfo.add("\nAdresa: 345 Vedľajšia ulica");
        customerInfo.add("\nMesto: Ružomberok");
        PdfPCell customerCell = new PdfPCell(customerInfo);
        customerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        customerCell.setBorder(Rectangle.NO_BORDER);
        tableCustomer.addCell(customerCell);


        Paragraph invoicingInfo = new Paragraph("Císlo faktúry: 12345\n");
        invoicingInfo.add("Dátum vystavenia: " + LocalDate.now().format(
                DateTimeFormatter.ofPattern("dd.MM.yyyy")));

        PdfPCell invoiceCell = new PdfPCell(invoicingInfo);
        invoiceCell.setBorder(Rectangle.NO_BORDER);
        tableCustomer.addCell(invoiceCell);

        document.add(tableCustomer);


        // Step 4: Add items to the invoice (example)
        document.add(Chunk.NEWLINE);
        document.add(new Paragraph("Produkty faktúry:",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
        document.add(Chunk.NEWLINE);
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.addCell("Položka");
        table.addCell("Pocet");
        table.addCell("Cena za kus");
        table.addCell("Pozícia v sklade");
        table.addCell(String.valueOf(reservations));
        table.addCell(price/reservations + " €");
        document.add(table);

        document.add(Chunk.NEWLINE);
        document.add(new Paragraph("Celková suma: " + price + " €",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 15)));

        document.add(Chunk.NEWLINE);
        document.add(new Paragraph("*Cena je vypočítaná podľa počtu rezervovnaých miest v sklade za každý" +
                "deň intervalu: " + changeDateFormat(dateFrom) + "-" + changeDateFormat(dateTo) + ".",
                FontFactory.getFont(FontFactory.TIMES_ROMAN, 10)));
    }

    private String changeDateFormat(String date){
        String year = date.substring(0, date.indexOf("-"));
        date = date.substring(date.indexOf("-")+1);
        String month = date.substring(0, date.indexOf("-"));
        String day = date.substring(date.indexOf("-")+1);
        return day + "." + month + "." + year;
    }

    public void exportOrderPDF(ObservableList<Map<String, String>> items, Customer customer) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("exports/Dodací list.pdf"));
            document.open();



            document.close();
            System.out.println("Order.pdf generated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
