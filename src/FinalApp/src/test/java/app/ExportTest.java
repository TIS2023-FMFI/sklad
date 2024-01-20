package app;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;
import java.io.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.util.Date;

public class ExportTest {
    @Test
    void exportData() throws IOException {
        // workbook object
        XSSFWorkbook workbook = new XSSFWorkbook();

        // spreadsheet object
        XSSFSheet spreadsheet
                = workbook.createSheet(" Student Data ");

        // creating a row object
        XSSFRow row;

        // This data needs to be written (Object[])
        Map<String, Object[]> studentData
                = new TreeMap<String, Object[]>();

        studentData.put(
                "1",
                new Object[] { "Roll No", "NAME", "Year" });

        studentData.put("2", new Object[] { "128", "Aditya",
                "2nd year" });

        studentData.put(
                "3",
                new Object[] { "129", "Narayana", "2nd year" });

        studentData.put("4", new Object[] { "130", "Mohan",
                "2nd year" });

        studentData.put("5", new Object[] { "131", "Radha",
                "2nd year" });

        studentData.put("6", new Object[] { "132", "Gopal",
                "2nd year" });

        Set<String> keyid = studentData.keySet();

        int rowid = 0;

        // writing the data into the sheets...

        for (String key : keyid) {
            row = spreadsheet.createRow(rowid++);
            Object[] objectArr = studentData.get(key);
            int cellid = 0;

            for (Object obj : objectArr) {
                Cell cell = row.createCell(cellid++);
                cell.setCellValue((String)obj);
            }
        }

        FileOutputStream out = new FileOutputStream(
                new File("exports/test.xlsx"));
        System.out.println("File created successfully.");
        workbook.write(out);
        out.close();
    }


    @Test
    public void createInvoicing() {
        try {
            // Step 1: Create a document
            Document document = new Document();
            // Step 2: Set the file path where the PDF will be saved
            PdfWriter.getInstance(document, new FileOutputStream("exports/Invoice.pdf"));
            // Step 3: Open the document for writing
            document.open();

            // Step 4: Add content to the document
            addMetaData(document);
            addInvoiceContent(document);

            // Step 5: Close the document
            document.close();
            System.out.println("Invoice generated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addMetaData(Document document) {
        document.addTitle("Faktúra");
        document.addSubject("Generate invoice using iText in Java");
        document.addKeywords("Java, iText, PDF, Invoice");
        document.addAuthor("Your Name");
        document.addCreator("Your Application");
    }

    private static void addInvoiceContent(Document document) throws DocumentException {
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

        Paragraph customerInfo = new Paragraph("Názov: Formula 1");
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
        document.add(new Paragraph("Invoice Items:",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
        document.add(Chunk.NEWLINE);
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.addCell("Položka");
        table.addCell("Pocet");
        table.addCell("Cena za kus");
        table.addCell("Pozícia v sklade");
        table.addCell("123");
        table.addCell("10.00 €");
        document.add(table);

        document.add(Chunk.NEWLINE);
        document.add(new Paragraph("Celková suma: 1230 €",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 15)));
    }
}
