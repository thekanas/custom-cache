package by.stolybko.service.impl;

import by.stolybko.exception.UserNotFoundException;
import by.stolybko.exception.ValidationException;
import by.stolybko.validator.Error;
import by.stolybko.dao.impl.UserDaoImpl;
import by.stolybko.dto.UserRequestDTO;
import by.stolybko.dto.UserResponseDTO;
import by.stolybko.entity.UserEntity;
import by.stolybko.mapper.UserMapper;
import by.stolybko.util.UserTestData;
import by.stolybko.validator.UserDtoValidator;
import by.stolybko.validator.ValidationResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserMapper mapper;

    @Mock
    private UserDaoImpl userDao;

    @Mock
    private UserDtoValidator validator;

    @Captor
    private ArgumentCaptor<UserEntity> userCaptor;

    @InjectMocks
    private UserServiceImpl userService;


    @Test
    void getUserByIdTest() {
        // given
        Long id = UserTestData.builder().build().getId();
        UserResponseDTO expected = UserTestData.builder()
                .build().buildUserResponseDTO();
        UserEntity userEntity = UserTestData.builder()
                .build().buildUser();

        when(mapper.toUserResponseDTO(userEntity))
                .thenReturn(expected);
        when(userDao.findById(id))
                .thenReturn(Optional.of(userEntity));

        // when
        UserResponseDTO actual = userService.getUserById(id);

        // then
        assertThat(actual).isEqualTo(expected);

    }

    @Test
    void getUserByIdShouldThrowException_whenUserNotFound() {
        // given
        Long id = UserTestData.builder().build().getId();

        when(userDao.findById(id))
                .thenReturn(Optional.empty());

        // when,then
        var userNotFoundException = assertThrows(UserNotFoundException.class, () -> userService.getUserById(id));
        assertThat(userNotFoundException.getMessage())
                .isEqualTo("User with id: " + id + " not found");
    }

    @Test
    void getAllTest() {
        // given
        UserResponseDTO userResponseDTO1 = UserTestData.builder()
                .withId(1L)
                .build().buildUserResponseDTO();
        UserResponseDTO userResponseDTO2 = UserTestData.builder()
                .withId(2L)
                .build().buildUserResponseDTO();
        UserEntity userEntity1 = UserTestData.builder()
                .withId(1L)
                .build().buildUser();
        UserEntity userEntity2 = UserTestData.builder()
                .withId(2L)
                .build().buildUser();


        List<UserResponseDTO> expected = List.of(userResponseDTO1, userResponseDTO2);
        List<UserEntity> users = List.of(userEntity1, userEntity2);

        when(mapper.toUserResponseDTO(userEntity1))
                .thenReturn(userResponseDTO1);
        when(mapper.toUserResponseDTO(userEntity2))
                .thenReturn(userResponseDTO2);
        when(userDao.findAll())
                .thenReturn(users);

        // when
        List<UserResponseDTO> actual = userService.getAll();

        // then
        assertIterableEquals(expected, actual);
    }

    @Test
    void saveTest() {
        // given
        UserEntity savedUserEntity = UserTestData.builder()
                .withId(null)
                .build().buildUser();
        UserEntity userEntity = UserTestData.builder()
                .build().buildUser();
        UserResponseDTO expected = UserTestData.builder()
                .build().buildUserResponseDTO();
        UserRequestDTO userRequestDTO = UserTestData.builder()
                .build().buildUserRequestDTO();

        when(validator.validate(userRequestDTO))
                .thenReturn(new ValidationResult());
        when(userDao.save(savedUserEntity))
                .thenReturn(Optional.ofNullable(userEntity));
        when(mapper.toUserEntity(userRequestDTO))
                .thenReturn(savedUserEntity);
        when(mapper.toUserResponseDTO(userEntity))
                .thenReturn(expected);

        // when
        UserResponseDTO actual = userService.save(userRequestDTO);

        // then
        verify(userDao).save(userCaptor.capture());
        assertThat(userCaptor.getValue())
                .hasFieldOrPropertyWithValue(UserEntity.Fields.id, null);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void saveShouldThrowException_whenDtoInvalid() {
        // given
        UserRequestDTO userRequestDTO = UserTestData.builder()
                .build().buildUserRequestDTO();
        ValidationResult validationResult = new ValidationResult();
        validationResult.add(Error.of("invalid.name", "message"));

        when(validator.validate(userRequestDTO))
                .thenReturn(validationResult);

        // when,then
        var validationException = assertThrows(ValidationException.class, () -> userService.save(userRequestDTO));
        assertThat(validationException.getErrors().get(0).getMessage())
                .isEqualTo(validationResult.getErrors().get(0).getMessage());
    }

    @Test
    void updateTest() {
        // given
        Long id = UserTestData.builder().build().getId();
        UserEntity updateUserEntity = UserTestData.builder()
                .withId(null)
                .build().buildUser();
        UserEntity userEntity = UserTestData.builder()
                .build().buildUser();
        UserResponseDTO expected = UserTestData.builder()
                .build().buildUserResponseDTO();
        UserRequestDTO userRequestDTO = UserTestData.builder()
                .build().buildUserRequestDTO();

        when(validator.validate(userRequestDTO))
                .thenReturn(new ValidationResult());
        when(userDao.update(userEntity))
                .thenReturn(Optional.of(userEntity));
        when(mapper.toUserEntity(userRequestDTO))
                .thenReturn(updateUserEntity);
        when(mapper.toUserResponseDTO(userEntity))
                .thenReturn(expected);

        // when
        UserResponseDTO actual = userService.update(userRequestDTO, id);

        // then
        verify(userDao).update(userCaptor.capture());
        assertThat(userCaptor.getValue())
                .isEqualTo(userEntity);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void updateShouldThrowException_whenDtoInvalid() {
        // given
        Long id = UserTestData.builder().build().getId();
        UserRequestDTO userRequestDTO = UserTestData.builder()
                .build().buildUserRequestDTO();
        ValidationResult validationResult = new ValidationResult();
        validationResult.add(Error.of("invalid.name", "message"));

        when(validator.validate(userRequestDTO))
                .thenReturn(validationResult);

        // when,then
        var validationException = assertThrows(ValidationException.class, () -> userService.update(userRequestDTO, id));
        assertThat(validationException.getErrors().get(0).getMessage())
                .isEqualTo(validationResult.getErrors().get(0).getMessage());
    }

    @Test
    void updateShouldThrowException_whenUserNotFound() {
        // given
        Long id = UserTestData.builder().build().getId();
        UserEntity updateUserEntity = UserTestData.builder()
                .withId(null)
                .build().buildUser();
        UserEntity userEntity = UserTestData.builder()
                .build().buildUser();
        UserRequestDTO userRequestDTO = UserTestData.builder()
                .build().buildUserRequestDTO();

        when(validator.validate(userRequestDTO))
                .thenReturn(new ValidationResult());
        when(userDao.update(userEntity))
                .thenReturn(Optional.empty());
        when(mapper.toUserEntity(userRequestDTO))
                .thenReturn(updateUserEntity);

        // when,then
        var userNotFoundException = assertThrows(UserNotFoundException.class, () -> userService.update(userRequestDTO, id));
        assertThat(userNotFoundException.getMessage())
                .isEqualTo("User with id: " + id + " not found");
    }


    @Test
    void deleteTest() {
        // given
        Long id = UserTestData.builder().build().getId();

        // when
        userService.delete(id);

        // then
        verify(userDao).delete(id);
    }
}
