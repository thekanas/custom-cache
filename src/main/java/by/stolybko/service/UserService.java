package by.stolybko.service;

import by.stolybko.dao.UserDao;
import by.stolybko.dto.UserRequestDTO;
import by.stolybko.dto.UserResponseDTO;
import by.stolybko.entity.UserEntity;
import by.stolybko.mapper.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserDao userDao = UserDao.getInstance();
    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    private static final UserService INSTANCE = new UserService();
    public static UserService getInstance() {
        return INSTANCE;
    }

    public UserResponseDTO getUserById(Long id) throws SQLException {

        Optional<UserEntity> user = userDao.findById(id);
        if(user.isEmpty()) {
            throw new SQLException();
        }
        return mapUserShowDTO(user.get());
    }

    public List<UserResponseDTO> getAll() {
        List<UserResponseDTO> users = new ArrayList<>();
        for(UserEntity user : userDao.findAll()) {
            users.add(mapUserShowDTO(user));
        }
        return users;
    }

    public UserResponseDTO save(UserRequestDTO user) {
        UserEntity userSawed = userDao.save(mapper.toUserEntity(user)).get();
        return mapper.toUserResponseDTO(userSawed);
    }

    public UserResponseDTO update(UserRequestDTO userDTO, Long id) {

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

    private UserResponseDTO mapUserShowDTO(UserEntity user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .passportNumber(user.getPassportNumber())
                .build();
    }

    private UserEntity mapUser (UserRequestDTO user) {
        return UserEntity.builder()
                .fullName(user.getFullName())
                .passportNumber(user.getPassportNumber())
                .password(user.getPassword())
                .build();
    }
}
