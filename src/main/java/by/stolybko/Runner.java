package by.stolybko;

import by.stolybko.api.DeSerializer;
import by.stolybko.api.Serializer;
import by.stolybko.api.impl.DeSerializerImpl;
import by.stolybko.api.impl.SerializerImpl;
import by.stolybko.config.LiquibaseConfig;
import by.stolybko.connection.ConnectionPool;
import by.stolybko.dao.Dao;
import by.stolybko.dao.impl.UserDaoImpl;
import by.stolybko.dto.UserRequestDTO;
import by.stolybko.dto.UserResponseDTO;
import by.stolybko.entity.UserEntity;
import by.stolybko.mapper.UserMapper;
import by.stolybko.printer.PDFPrinter;
import by.stolybko.printer.Printer;
import by.stolybko.service.impl.UserServiceImpl;
import by.stolybko.validator.UserDtoValidator;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.forms.fields.PdfTextFormField;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import liquibase.exception.LiquibaseException;
import org.mapstruct.factory.Mappers;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Runner {
    public static void main(String[] args) throws LiquibaseException, IOException, IllegalAccessException {
        LiquibaseConfig.migrate();

        final Dao<Long, UserEntity> userDao = UserDaoImpl.getInstance();
        final UserMapper mapper = Mappers.getMapper(UserMapper.class);
        final UserDtoValidator validator = UserDtoValidator.getInstance();

        UserServiceImpl userService = new UserServiceImpl(userDao, mapper, validator);

        String jsonRequest = """
                {
                    "fullName": "Yurii Sergeevich Ivanov",
                    "passportNumber" : "0308767HB143",
                    "password": "qwerty",
                    "email": "googl@googl.com"
                }
                      """;

        DeSerializer deSerializer = new DeSerializerImpl();
        UserRequestDTO userRequestDTO = deSerializer.deSerializingJson(UserRequestDTO.class, jsonRequest);

        UserResponseDTO userResponseDTO = userService.save(userRequestDTO);
        userService.delete(userResponseDTO.getId());

        Serializer serializer = new SerializerImpl();
        String jsonResponse = serializer.serializingInJson(userResponseDTO);

//        Document document = new Document();
//        PdfWriter.getInstance(document, new FileOutputStream("iText.pdf"));
//
//        document.open();
        /*Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
        Chunk chunkFirst = new Chunk(userResponseDTO.getId() + "\n", font);
        Chunk chunkSecond = new Chunk(userResponseDTO.getFullName() + "\n", font);
        Chunk chunkThird = new Chunk(userResponseDTO.getPassportNumber() + "\n", font);

        document.add(chunkFirst);
        document.add(chunkSecond);
        document.add(chunkThird);*/

        Printer printer = new PDFPrinter();
        //System.out.println(userService.getAll());
        printer.printTable(userService.getAll());


        /*PdfReader reader = new PdfReader("img/Clevertec_Template-1.pdf");
        PdfWriter writer = new PdfWriter("iText.pdf");
        PdfDocument pdfDocument = new PdfDocument(reader, writer);
        //addContentToDocument(pdfDocument);
        Document document = new Document(pdfDocument);

        Table table = new Table(UnitValue.createPercentArray(3)).setMarginTop(110);

        table.addHeaderCell("id");
        table.addHeaderCell("fullName");
        table.addHeaderCell("passportNumber");

        for(UserResponseDTO user : userService.getAll()) {
            table.addCell(user.getId().toString());
            table.addCell(user.getFullName());
            table.addCell(user.getPassportNumber());
        }
        document.add(table);

        document.close();*/
        /*PdfPTable table = new PdfPTable(3);
        addTableHeader(table);
        for(UserResponseDTO user : userService.getAll()) {
            addRows(table, user);
            //addCustomRows(table);
        }*/

        //pdfDocument.close();
//        document.add(table);
//        document.close();
        //userService.delete(userResponseDTO.getId());
        System.out.println(jsonResponse);

    }


}
