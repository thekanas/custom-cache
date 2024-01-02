package by.stolybko.validator;

import by.stolybko.dto.UserRequestDTO;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import static lombok.AccessLevel.PRIVATE;

/**
 * Объект для валидации входящих представлений-запросов информации клиентов.
 */
@Component
@NoArgsConstructor(access = PRIVATE)
public class UserDtoValidator implements Validator<UserRequestDTO> {

    private static final UserDtoValidator INSTANCE = new UserDtoValidator();

    public static UserDtoValidator getInstance() {
        return INSTANCE;
    }

    @Override
    public ValidationResult validate(UserRequestDTO userRequestDTO) {

        var validationResult = new ValidationResult();

        if (userRequestDTO.getFullName() == null || userRequestDTO.getFullName().isEmpty()) {
            validationResult.add(Error.of("invalid.name", "Name cannot be null or empty"));
        }
        else if (!userRequestDTO.getFullName().matches("[A-z\\s]{5,60}")) {
            validationResult.add(Error.of("invalid.name", "Name must contain 5-60 characters (Latin or spaces)"));
        }

        if (userRequestDTO.getPassportNumber() == null || userRequestDTO.getPassportNumber().isEmpty()) {
            validationResult.add(Error.of("invalid.passportNumber", "Passport number cannot be null or empty"));
        }

        if (userRequestDTO.getPassword() == null || userRequestDTO.getPassword().isEmpty()) {
            validationResult.add(Error.of("invalid.password", "Password cannot be null or empty"));
        }

        if (userRequestDTO.getEmail() == null || userRequestDTO.getEmail().isEmpty()) {
            validationResult.add(Error.of("invalid.email", "Email cannot be null or empty"));
        }
        else if (!userRequestDTO.getEmail().matches("^([a-z0-9_.-]+)@([\\da-z.-]+)\\.([a-z.]{2,6})$")) {
            validationResult.add(Error.of("invalid.email", "Email must be correct"));
        }

        return validationResult;
    }
}
