package by.stolybko.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Представление-ответ информации клиента.
 */
@Data
@Builder
@EqualsAndHashCode(of = "id")
public class UserResponseDTO {
        private Long id;
        private String fullName;
        private String passportNumber;
}
