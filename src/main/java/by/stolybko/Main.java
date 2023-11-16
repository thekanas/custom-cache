package by.stolybko;

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
    }
}