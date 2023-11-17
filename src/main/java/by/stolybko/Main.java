package by.stolybko;

import by.stolybko.dto.UserRequestDTO;
import by.stolybko.service.UserService;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        UserService userService = UserService.getInstance();


        System.out.println(userService.getUserById(1L));
        System.out.println(userService.getUserById(2L));
        System.out.println(userService.getUserById(1L));
        System.out.println(userService.getUserById(2L));
        System.out.println(userService.getUserById(3L));

        UserRequestDTO userDTO = UserRequestDTO.builder()
                .fullName("Test")
                .passportNumber("13245")
                .password("1234")
                .email("ggg@ggg1.ru")
                .build();
        System.out.println();
        userService.save(userDTO);
        System.out.println(userService.getUserById(22L));
    }
}