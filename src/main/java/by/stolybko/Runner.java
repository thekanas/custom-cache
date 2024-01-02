package by.stolybko;

import by.stolybko.api.DeSerializer;
import by.stolybko.api.Serializer;
import by.stolybko.api.impl.DeSerializerImpl;
import by.stolybko.api.impl.SerializerImpl;
import by.stolybko.config.AppConfig;
import by.stolybko.dto.UserRequestDTO;
import by.stolybko.dto.UserResponseDTO;
import by.stolybko.printer.Printer;
import by.stolybko.printer.PrinterType;
import by.stolybko.service.impl.UserServiceImpl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Runner {
    public static void main(String[] args) {

        var context = new AnnotationConfigApplicationContext(AppConfig.class);

        UserServiceImpl userService = context.getBean("userService", UserServiceImpl.class);

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

        Printer printer = context.getBean("printer", Printer.class);
        printer.print(userService.getAll(), PrinterType.PDF);
        printer.print(userResponseDTO, PrinterType.PDF);

        System.out.println(jsonResponse);

    }
}
