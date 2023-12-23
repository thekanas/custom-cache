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
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * сервисный класс клиентов банковской системы
 */
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final Dao<Long, UserEntity> userDao;
    private final UserMapper mapper;
    private final UserDtoValidator validator;

    public UserServiceImpl() {
        userDao = UserDaoImpl.getInstance();
        mapper = Mappers.getMapper(UserMapper.class);
        validator = UserDtoValidator.getInstance();
    }

    /**
     * Возвращает представление клиента по его идентификатору.
     *
     * @param id идентификатор клиента.
     * @return представление клиента.
     * @throws UserNotFoundException клиент не найден в базе данных
     */
    @Override
    public UserResponseDTO getUserById(Long id) {

        Optional<UserEntity> user = userDao.findById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException(id);
        }
        return mapper.toUserResponseDTO(user.get());
    }

    /**
     * Возвращает представления всех клиентов.
     *
     * @return список представлений клиентов.
     */
    @Override
    public List<UserResponseDTO> getAll() {
        List<UserResponseDTO> users = new ArrayList<>();
        for (UserEntity user : userDao.findAll()) {
            users.add(mapper.toUserResponseDTO(user));
        }
        return users;
    }

    /**
     * Возвращает представления всех клиентов.
     *
     * @return список представлений клиентов.
     */
    @Override
    public List<UserResponseDTO> getAll(int limit, int offset) {
        List<UserResponseDTO> users = new ArrayList<>();
        for (UserEntity user : userDao.findAll(limit, offset)) {
            users.add(mapper.toUserResponseDTO(user));
        }
        return users;
    }

    /**
     * Сохраняет клиента в базе данных.
     *
     * @param user представление-запрос клиента.
     * @return представление-ответ клиента.
     * @throws ValidationException запрос не прошел валидацию
     */
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

    /**
     * Обновляет информацию о клиенте в базе данных.
     *
     * @param userDTO представление-запрос клиента.
     * @param id идентификатор обновляемого клиента.
     * @return представление-ответ клиента.
     * @throws ValidationException запрос не прошел валидацию
     * @throws UserNotFoundException клиент не найден в базе данных
     */
    @Override
    public UserResponseDTO update(UserRequestDTO userDTO, Long id) {
        ValidationResult validationResult = validator.validate(userDTO);
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getErrors());
        }
        UserEntity user = mapper.toUserEntity(userDTO);
        user.setId(id);

        Optional<UserEntity> userSawed = userDao.update(user);
        if (userSawed.isEmpty()) {
            throw new UserNotFoundException(id);
        }
        return mapper.toUserResponseDTO(userSawed.get());
    }

    /**
     * Удаляет клиента из базы данных.
     *
     * @param id id идентификатор удаляемого клиента.
     */
    @Override
    public void delete(Long id) {
        userDao.delete(id);
    }
}
