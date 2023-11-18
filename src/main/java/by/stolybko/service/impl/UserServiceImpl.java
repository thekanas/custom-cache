package by.stolybko.service.impl;

import by.stolybko.dao.UserDao;
import by.stolybko.dto.UserRequestDTO;
import by.stolybko.dto.UserResponseDTO;
import by.stolybko.entity.UserEntity;
import by.stolybko.exception.UserNotFoundException;
import by.stolybko.mapper.UserMapper;
import by.stolybko.service.UserService;
import org.mapstruct.factory.Mappers;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    private final UserDao userDao = UserDao.getInstance();
    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    private static final UserServiceImpl INSTANCE = new UserServiceImpl();
    public static UserServiceImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public UserResponseDTO getUserById(Long id) {

        Optional<UserEntity> user = userDao.findById(id);
        if(user.isEmpty()) {
            throw new UserNotFoundException(id);
        }
        return mapper.toUserResponseDTO(user.get());
    }

    @Override
    public List<UserResponseDTO> getAll() {
        List<UserResponseDTO> users = new ArrayList<>();
        for(UserEntity user : userDao.findAll()) {
            users.add(mapper.toUserResponseDTO(user));
        }
        return users;
    }

    @Override
    public UserResponseDTO save(UserRequestDTO user) {
        UserEntity userSawed = userDao.save(mapper.toUserEntity(user)).get();
        return mapper.toUserResponseDTO(userSawed);
    }

    @Override
    public UserResponseDTO update(UserRequestDTO userDTO, Long id) {

        UserEntity user = mapper.toUserEntity(userDTO);
        user.setId(id);

        UserEntity userSawed = userDao.update(user).get();
        return mapper.toUserResponseDTO(userSawed);
    }

    @Override
    public void delete(Long id) {
        userDao.delete(id);
    }
}
