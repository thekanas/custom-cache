package by.stolybko.util;

import by.stolybko.dto.UserRequestDTO;
import by.stolybko.dto.UserResponseDTO;
import by.stolybko.entity.UserEntity;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder(setterPrefix = "with", toBuilder = true)
@Accessors(chain = true)
public class UserTestData {

    @Builder.Default
    private Long id = 9L;

    @Builder.Default
    private String fullName = "Evgenii Petrovich Bazylev";

    @Builder.Default
    private String passportNumber = "308767HB126";

    @Builder.Default
    private String email = "email7@email.com";

    @Builder.Default
    private String password = "0123";

    public UserEntity buildUser() {
        return UserEntity.builder()
                .id(id)
                .fullName(fullName)
                .passportNumber(passportNumber)
                .email(email)
                .password(password)
                .build();
    }

    public UserRequestDTO buildUserRequestDTO() {
        return UserRequestDTO.builder()
                .fullName(fullName)
                .passportNumber(passportNumber)
                .email(email)
                .password(password)
                .build();
    }

    public UserResponseDTO buildUserResponseDTO() {
        return UserResponseDTO.builder()
                .id(id)
                .fullName(fullName)
                .passportNumber(passportNumber)
                .build();
    }
}
