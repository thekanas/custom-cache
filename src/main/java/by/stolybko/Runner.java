package by.stolybko;

import by.stolybko.api.DeSerializer;
import by.stolybko.api.Serializer;
import by.stolybko.api.impl.DeSerializerImpl;
import by.stolybko.api.impl.SerializerImpl;
import by.stolybko.config.LiquibaseConfig;
import by.stolybko.dao.Dao;
import by.stolybko.dao.impl.UserDaoImpl;
import by.stolybko.dto.UserRequestDTO;
import by.stolybko.dto.UserResponseDTO;
import by.stolybko.entity.UserEntity;
import by.stolybko.mapper.UserMapper;
import by.stolybko.printer.Printer;
import by.stolybko.printer.PrinterType;
import by.stolybko.service.impl.UserServiceImpl;
import by.stolybko.validator.UserDtoValidator;
import liquibase.exception.LiquibaseException;
import org.mapstruct.factory.Mappers;


public class Runner {
    public static void main(String[] args) throws LiquibaseException {
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

        Printer printer = new Printer();
        printer.print(userService.getAll(), PrinterType.PDF);
        printer.print(userResponseDTO, PrinterType.PDF);

        System.out.println(jsonResponse);

    }
}
