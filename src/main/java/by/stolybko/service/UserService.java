package by.stolybko.service;

import by.stolybko.dao.UserDao;
import by.stolybko.dto.UserDTO;
import by.stolybko.dto.UserShowDTO;
import by.stolybko.entity.UserEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserDao userDao = UserDao.getInstance();

    private static final UserService INSTANCE = new UserService();
    public static UserService getInstance() {
        return INSTANCE;
    }

    public UserShowDTO getUserById(Long id) throws SQLException {

        Optional<UserEntity> user = userDao.findById(id);
        if(user.isEmpty()) {
            throw new SQLException();
        }
        return mapUserShowDTO(user.get());
    }

    public List<UserShowDTO> getAll() {
        List<UserShowDTO> users = new ArrayList<>();
        for(UserEntity user : userDao.findAll()) {
            users.add(mapUserShowDTO(user));
        }
        return users;
    }

    public UserShowDTO save(UserDTO user) {
        UserEntity userSawed = userDao.save(mapUser(user)).get();
        return mapUserShowDTO(userSawed);
    }

    public UserShowDTO update(UserDTO userDTO, Long id) {

        UserEntity user = UserEntity.builder()
                .id(id)
                .fullName(userDTO.getFullName())
                .passportNumber(userDTO.getPassportNumber())
                .password(userDTO.getPassword())
                .build();

        UserEntity userSawed = userDao.update(user).get();
        return mapUserShowDTO(userSawed);
    }

    public boolean delete(Long id) {
        if(userDao.findById(id).isEmpty()){
            return false;
        }
        return userDao.delete(id);
    }

    private UserShowDTO mapUserShowDTO(UserEntity user) {
        return UserShowDTO.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .passportNumber(user.getPassportNumber())
                .build();
    }

    private UserEntity mapUser (UserDTO user) {
        return UserEntity.builder()
                .fullName(user.getFullName())
                .passportNumber(user.getPassportNumber())
                .password(user.getPassword())
                .build();
    }
}
