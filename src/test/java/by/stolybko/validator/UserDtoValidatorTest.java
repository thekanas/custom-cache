package by.stolybko.validator;


import by.stolybko.dto.UserRequestDTO;
import by.stolybko.util.UserTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

class UserDtoValidatorTest {

    Validator<UserRequestDTO> validator = UserDtoValidator.getInstance();

    @Test
    void shouldPassValidation() {
        // given
        UserRequestDTO userDTO = UserTestData.builder()
                .build().buildUserRequestDTO();

        // when
        ValidationResult actualResult = validator.validate(userDTO);

        // then
        assertFalse(actualResult.hasErrors());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"Ян Михайлович", "Yan", "123456", " "})
    void invalidFullName(String name) {
        // given
        UserRequestDTO userDTO = UserTestData.builder()
                .withFullName(name)
                .build().buildUserRequestDTO();

        // when
        ValidationResult actualResult = validator.validate(userDTO);

        // then
        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo("invalid.name");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"google.com", "@google.com", "google@google", "google@", " "})
    void invalidEmail(String email) {
        // given
        UserRequestDTO userDTO = UserTestData.builder()
                .withEmail(email)
                .build().buildUserRequestDTO();

        // when
        ValidationResult actualResult = validator.validate(userDTO);

        // then
        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo("invalid.email");
    }

    @Test
    void invalidPasswordIfEmpty() {
        // given
        UserRequestDTO userDTO = UserTestData.builder()
                .withPassword("")
                .build().buildUserRequestDTO();

        // when
        ValidationResult actualResult = validator.validate(userDTO);

        // then
        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo("invalid.password");
    }

    @Test
    void invalidPassportNumberIfEmpty() {
        // given
        UserRequestDTO userDTO = UserTestData.builder()
                .withPassportNumber("")
                .build().buildUserRequestDTO();

        // when
        ValidationResult actualResult = validator.validate(userDTO);

        // then
        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo("invalid.passportNumber");
    }

}