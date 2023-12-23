package by.stolybko.printer.impl;

import by.stolybko.printer.PrinterExecutor;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import lombok.SneakyThrows;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;

public class PDFPrinter implements PrinterExecutor {

    private final String template = "out/template.pdf";


    @SneakyThrows
    @Override
    public void printEntity(Object obj, String path) {

        try (PdfReader reader = new PdfReader(Objects.requireNonNull(PDFPrinter.class.getClassLoader().getResourceAsStream(template)));
             PdfWriter writer = new PdfWriter(path);
             PdfDocument pdfDocument = new PdfDocument(reader, writer);
             Document document = new Document(pdfDocument)) {

            doEntityBody(obj, document);
        }
    }

    @SneakyThrows
    @Override
    public void printTable(List<?> objList, String path) {

        try (PdfReader reader = new PdfReader(Objects.requireNonNull(PDFPrinter.class.getClassLoader().getResourceAsStream(template)));
             PdfWriter writer = new PdfWriter(path);
             PdfDocument pdfDocument = new PdfDocument(reader, writer);
             Document document = new Document(pdfDocument)) {

            doTableBody(objList, document);
        }
    }

    @SneakyThrows
    @Override
    public byte[] getEntityAsBytes(Object obj) {

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             PdfReader reader = new PdfReader(Objects.requireNonNull(PDFPrinter.class.getClassLoader().getResourceAsStream(template)));
             PdfWriter writer = new PdfWriter(outputStream);
             PdfDocument pdfDocument = new PdfDocument(reader, writer);
             Document document = new Document(pdfDocument)) {

            doEntityBody(obj, document);
            document.close();
            return outputStream.toByteArray();
        }
    }

    @SneakyThrows
    @Override
    public byte[] getTableAsBytes(List<?> objList) {

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             PdfReader reader = new PdfReader(Objects.requireNonNull(PDFPrinter.class.getClassLoader().getResourceAsStream(template)));
             PdfWriter writer = new PdfWriter(outputStream);
             PdfDocument pdfDocument = new PdfDocument(reader, writer);
             Document document = new Document(pdfDocument)) {

            doTableBody(objList, document);
            document.close();
            return outputStream.toByteArray();
        }
    }

    private void doEntityBody(Object obj, Document document) throws IntrospectionException, IllegalAccessException, InvocationTargetException {

        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();

        Paragraph paragraph = new Paragraph("Information about:").setMarginTop(150).setBold();
        document.add(paragraph);

        for (Field field : fields) {
            PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
            Paragraph paragraphNext = new Paragraph(pd.getName() + ": " + pd.getReadMethod().invoke(obj));
            document.add(paragraphNext);
        }
    }

    private void doTableBody(List<?> objList, Document document) throws IntrospectionException, IllegalAccessException, InvocationTargetException {

        Class<?> clazz = objList.get(0).getClass();
        Field[] fields = clazz.getDeclaredFields();

        Paragraph paragraph = new Paragraph("All information").setMarginTop(150).setBold();
        document.add(paragraph);

        Table table = new Table(fields.length);

        for (Field field : fields) {
            table.addHeaderCell(field.getName());
        }

        for (Object obj : objList) {

            for (Field field : fields) {
                PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
                table.addCell("" + pd.getReadMethod().invoke(obj));
            }
        }
        document.add(table);
    }
}
