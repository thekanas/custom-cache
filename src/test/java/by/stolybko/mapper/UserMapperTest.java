package by.stolybko.mapper;

import by.stolybko.dto.UserRequestDTO;
import by.stolybko.dto.UserResponseDTO;
import by.stolybko.entity.UserEntity;
import by.stolybko.util.UserTestData;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;


class UserMapperTest {

    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Test
    void toUserEntity() {
        // given
        UserRequestDTO userDTO = UserTestData.builder()
                .build().buildUserRequestDTO();

        // when
        UserEntity actual = mapper.toUserEntity(userDTO);

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue(UserEntity.Fields.id, null)
                .hasFieldOrPropertyWithValue(UserEntity.Fields.fullName, userDTO.getFullName())
                .hasFieldOrPropertyWithValue(UserEntity.Fields.passportNumber, userDTO.getPassportNumber())
                .hasFieldOrPropertyWithValue(UserEntity.Fields.password, userDTO.getPassword())
                .hasFieldOrPropertyWithValue(UserEntity.Fields.email, userDTO.getEmail());
    }

    @Test
    void toUserResponseDTO() {
        // given
        UserEntity userEntity = UserTestData.builder()
                .build().buildUser();

        // when
        UserResponseDTO actual = mapper.toUserResponseDTO(userEntity);

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue(UserEntity.Fields.id, userEntity.getId())
                .hasFieldOrPropertyWithValue(UserEntity.Fields.fullName, userEntity.getFullName())
                .hasFieldOrPropertyWithValue(UserEntity.Fields.passportNumber, userEntity.getPassportNumber());
    }

}