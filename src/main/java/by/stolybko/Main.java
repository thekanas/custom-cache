package by.stolybko;

import by.stolybko.connection.ConnectionPool;
import by.stolybko.dto.UserRequestDTO;
import by.stolybko.service.impl.UserServiceImpl;
import liquibase.Liquibase;
import liquibase.exception.LiquibaseException;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, LiquibaseException {
        ConnectionPool.migrate();

//    private final Dao<Long, UserEntity> userDao = UserDaoImpl.getInstance();
//    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);
//    private final UserDtoValidator validator = UserDtoValidator.getInstance();
//        UserServiceImpl userService = UserServiceImpl.getInstance();
//        Long id = 31L;
//
//        System.out.println(userService.getUserById(1L));
//        System.out.println(userService.getUserById(2L));
//        System.out.println(userService.getUserById(1L));
//        System.out.println(userService.getUserById(2L));
//        System.out.println(userService.getUserById(3L));
//        //userService.delete(11L);
//        UserRequestDTO userDTO = UserRequestDTO.builder()
//                .fullName("Testtyy")
//                .passportNumber("13245")
//                .password("1234")
//                .email("ggg@ggg.ru")
//                .build();
//        System.out.println();
//        userService.save(userDTO);
//        System.out.println(userService.getUserById(id));
//        userService.delete(id);
    }
}