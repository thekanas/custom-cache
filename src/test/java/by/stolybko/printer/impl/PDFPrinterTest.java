package by.stolybko.printer.impl;

import by.stolybko.dto.UserResponseDTO;
import by.stolybko.printer.PrinterExecutor;
import by.stolybko.util.UserTestData;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.kernel.pdf.canvas.parser.listener.ITextExtractionStrategy;
import com.itextpdf.kernel.pdf.canvas.parser.listener.SimpleTextExtractionStrategy;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


class PDFPrinterTest {

    @Test
    void printEntityTest() throws IOException {
        // given
        PrinterExecutor printer = new PDFPrinter();
        UserResponseDTO responseDTO = UserTestData.builder()
                .build().buildUserResponseDTO();
        String expected = "Information about:" + "\n" +
                "id: " + responseDTO.getId() + "\n" +
                "fullName: " + responseDTO.getFullName() + "\n" +
                "passportNumber: " + responseDTO.getPassportNumber();

        // when
        printer.printEntity(responseDTO, "src/test/resources/pdfTest/");

        // then
        assertDoesNotThrow(() -> new PdfReader("src/test/resources/pdfTest/-UserResponseDTO.pdf"));

        PdfReader pdfReader = new PdfReader("src/test/resources/pdfTest/-UserResponseDTO.pdf");
        PdfDocument pdfDocument = new PdfDocument(pdfReader);
        ITextExtractionStrategy extractionStrategy = new SimpleTextExtractionStrategy();
        String actual = PdfTextExtractor.getTextFromPage(pdfDocument.getPage(1), extractionStrategy);
        pdfDocument.close();
        pdfReader.close();
        //Files.delete(Paths.get("src/test/resources/pdfTest/-UserResponseDTO.pdf"));

        assertThat(actual.trim()).isEqualTo(expected);
    }

    @Test
    void printTableTest() throws IOException {
        // given
        PrinterExecutor printer = new PDFPrinter();
        UserResponseDTO responseDTO1 = UserTestData.builder()
                .withId(1L)
                .build().buildUserResponseDTO();
        UserResponseDTO responseDTO2 = UserTestData.builder()
                .withId(2L)
                .build().buildUserResponseDTO();

        System.out.println(responseDTO1);

        String expected = "All information" + "\n" +
                "id fullName passportNumber" + "\n" +
                responseDTO1.getId() + " " +
                responseDTO1.getFullName() + " " +
                responseDTO1.getPassportNumber() + "\n" +
                responseDTO2.getId() + " " +
                responseDTO2.getFullName() + " " +
                responseDTO2.getPassportNumber();

        // when
        printer.printTable(List.of(responseDTO1, responseDTO2), "src/test/resources/pdfTest/");

        // then
        assertDoesNotThrow(() -> new PdfReader("src/test/resources/pdfTest/-UserResponseDTO-table.pdf"));

        PdfReader pdfReader = new PdfReader("src/test/resources/pdfTest/-UserResponseDTO-table.pdf");
        PdfDocument pdfDocument = new PdfDocument(pdfReader);
        ITextExtractionStrategy extractionStrategy = new SimpleTextExtractionStrategy();
        String actual = PdfTextExtractor.getTextFromPage(pdfDocument.getPage(1), extractionStrategy);
        pdfDocument.close();
        pdfReader.close();
        //Files.delete(Paths.get("src/test/resources/pdfTest/-UserResponseDTO-table.pdf"));

        assertThat(actual.trim()).isEqualTo(expected);
    }
}
