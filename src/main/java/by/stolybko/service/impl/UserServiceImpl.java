package by.stolybko.service.impl;

import by.stolybko.dao.Dao;
import by.stolybko.dao.impl.UserDaoImpl;
import by.stolybko.dto.UserRequestDTO;
import by.stolybko.dto.UserResponseDTO;
import by.stolybko.entity.UserEntity;
import by.stolybko.exception.UserNotFoundException;
import by.stolybko.exception.ValidationException;
import by.stolybko.mapper.UserMapper;
import by.stolybko.service.UserService;
import by.stolybko.validator.UserDtoValidator;
import by.stolybko.validator.ValidationResult;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    private final Dao<Long, UserEntity> userDao = UserDaoImpl.getInstance();
    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);
    private final UserDtoValidator validator = UserDtoValidator.getInstance();

    private static final UserServiceImpl INSTANCE = new UserServiceImpl();

    public static UserServiceImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public UserResponseDTO getUserById(Long id) {

        Optional<UserEntity> user = userDao.findById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException(id);
        }
        return mapper.toUserResponseDTO(user.get());
    }

    @Override
    public List<UserResponseDTO> getAll() {
        List<UserResponseDTO> users = new ArrayList<>();
        for (UserEntity user : userDao.findAll()) {
            users.add(mapper.toUserResponseDTO(user));
        }
        return users;
    }

    @Override
    public UserResponseDTO save(UserRequestDTO user) {
        ValidationResult validationResult = validator.validate(user);
        if (validationResult.hasErrors()) {
            System.out.println(validationResult.getErrors());
            throw new ValidationException(validationResult.getErrors());
        }
        UserEntity userSawed = userDao.save(mapper.toUserEntity(user)).get();
        return mapper.toUserResponseDTO(userSawed);
    }

    @Override
    public UserResponseDTO update(UserRequestDTO userDTO, Long id) {
        ValidationResult validationResult = validator.validate(userDTO);
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getErrors());
        }
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
