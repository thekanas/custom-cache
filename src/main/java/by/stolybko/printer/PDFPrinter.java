package by.stolybko.printer;

import by.stolybko.dto.UserResponseDTO;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.styledxmlparser.jsoup.select.Evaluator;
import lombok.SneakyThrows;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class PDFPrinter extends Printer {


    @SneakyThrows
    @Override
    public void printEntity(Object obj) {

        Document document = null;
        try {

            PdfReader reader = new PdfReader("img/Clevertec_Template-1.pdf");
            PdfWriter writer = new PdfWriter("iText.pdf");
            PdfDocument pdfDocument = new PdfDocument(reader, writer);

            document = new Document(pdfDocument);

            Paragraph paragraph = new Paragraph("Information about:").setMarginTop(150).setBold();
            document.add(paragraph);
            for (PropertyDescriptor pd : Introspector.getBeanInfo(obj.getClass()).getPropertyDescriptors()) {
                if (pd.getReadMethod() != null && !"class".equals(pd.getName())) {
                    System.out.println(pd);
                    Paragraph paragraphNext = new Paragraph(pd.getName() + ": " + pd.getReadMethod().invoke(obj));
                    document.add(paragraphNext);
                }
            }
        } finally {
            if (document != null) document.close();
        }

    }

    @SneakyThrows
    @Override
    public void printTable(List<?> objectList) {

        Document document = null;

        try {
            PdfReader reader = new PdfReader("img/Clevertec_Template-1.pdf");
            PdfWriter writer = new PdfWriter("iText.pdf");
            PdfDocument pdfDocument = new PdfDocument(reader, writer);

            document = new Document(pdfDocument);

            Paragraph paragraph = new Paragraph("Information about:").setMarginTop(150).setBold();
            document.add(paragraph);

            Class clazz = objectList.get(0).getClass();

            Field[] fields = clazz.getDeclaredFields();

            System.out.println();
            Table table = new Table(fields.length);

            for (Field field : fields) {
                table.addHeaderCell(field.getName());
            }

            for (Object obj : objectList) {
                for (PropertyDescriptor pd : Introspector.getBeanInfo(obj.getClass()).getPropertyDescriptors()) {
                    if (pd.getReadMethod() != null && !"class".equals(pd.getName())) {
                        //System.out.println(pd);
                        table.addCell(pd.getReadMethod().invoke(obj).toString());
                    }
                }
            }
            document.add(table);
        } finally {
            if (document != null) document.close();
        }

    }
}
