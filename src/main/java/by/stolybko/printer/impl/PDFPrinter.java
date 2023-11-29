package by.stolybko.printer.impl;

import by.stolybko.printer.PrinterExecutor;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import lombok.SneakyThrows;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.List;

public class PDFPrinter implements PrinterExecutor {


    @SneakyThrows
    @Override
    public void printEntity(Object obj, String path) {

        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();

        try(PdfReader reader = new PdfReader( "output/template.pdf");
            PdfWriter writer = new PdfWriter(path + "-" + clazz.getSimpleName() + ".pdf");
            PdfDocument pdfDocument = new PdfDocument(reader, writer);
            Document document = new Document(pdfDocument)) {

            Paragraph paragraph = new Paragraph("Information about:").setMarginTop(150).setBold();
            document.add(paragraph);

            for (Field field : fields) {
                PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
                Paragraph paragraphNext = new Paragraph(pd.getName() + ": " + pd.getReadMethod().invoke(obj));
                document.add(paragraphNext);
            }
        }
    }

    @SneakyThrows
    @Override
    public void printTable(List<?> objectList, String path) {

        Class<?> clazz = objectList.get(0).getClass();
        Field[] fields = clazz.getDeclaredFields();

        try(PdfReader reader = new PdfReader("output/template.pdf");
            PdfWriter writer = new PdfWriter(path + "-" + clazz.getSimpleName() + "-table" + ".pdf");
            PdfDocument pdfDocument = new PdfDocument(reader, writer);
            Document document = new Document(pdfDocument)) {

            Paragraph paragraph = new Paragraph("All information").setMarginTop(150).setBold();
            document.add(paragraph);

            Table table = new Table(fields.length);

            for (Field field : fields) {
                table.addHeaderCell(field.getName());
            }

            for (Object obj : objectList) {

                for (Field field : fields) {
                    PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
                    table.addCell(pd.getReadMethod().invoke(obj).toString());
                }
            }

            document.add(table);

        }
    }
}
