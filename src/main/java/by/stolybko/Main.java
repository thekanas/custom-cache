package by.stolybko;

import by.stolybko.connection.ConnectionPool;
import by.stolybko.dao.Dao;
import by.stolybko.dao.impl.UserDaoImpl;
import by.stolybko.dto.UserRequestDTO;
import by.stolybko.dto.UserResponseDTO;
import by.stolybko.entity.UserEntity;
import by.stolybko.mapper.UserMapper;
import by.stolybko.service.impl.UserServiceImpl;
import by.stolybko.validator.UserDtoValidator;
import liquibase.Liquibase;
import liquibase.exception.LiquibaseException;
import org.mapstruct.factory.Mappers;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws LiquibaseException {
        ConnectionPool.migrate();

        final Dao<Long, UserEntity> userDao = UserDaoImpl.getInstance();
        final UserMapper mapper = Mappers.getMapper(UserMapper.class);
        final UserDtoValidator validator = UserDtoValidator.getInstance();
        UserServiceImpl userService = new UserServiceImpl(userDao, mapper, validator);

        System.out.println(userService.getUserById(1L));
        System.out.println(userService.getUserById(2L));
        System.out.println(userService.getUserById(1L));
        System.out.println(userService.getUserById(2L));
        System.out.println(userService.getUserById(3L));

        UserRequestDTO userDTO = UserRequestDTO.builder()
                .fullName("Testtyy")
                .passportNumber("13245")
                .password("1234")
                .email("ggg@ggg.ru")
                .build();
        System.out.println();
        UserResponseDTO userResponseDTO = userService.save(userDTO);

        System.out.println(userService.getUserById(userResponseDTO.getId()));
        userService.delete(userResponseDTO.getId());
    }
}