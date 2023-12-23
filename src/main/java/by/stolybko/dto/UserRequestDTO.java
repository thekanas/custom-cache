package by.stolybko.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Представление-запрос информации клиента.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {
    private String fullName;
    private String passportNumber;
    private String password;
    private String email;
}
