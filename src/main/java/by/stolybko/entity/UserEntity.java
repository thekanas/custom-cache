package by.stolybko.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldNameConstants;

/**
 * Представление информации о клиенте в базе данных.
 */
@Data
@Builder
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class UserEntity extends BaseEntity {

    /**
     * Идентификатор клиента (генерируется базой)
     */
    private Long id;

    /**
     * ФИО клиента (не может быть null или пустым, содержит 5-60 символов(латинский или пробелы))
     */
    @NonNull
    private String fullName;

    /**
     * номер паспорта клиента (не может быть null или пустым)
     */
    @NonNull
    private String passportNumber;

    /**
     * емаил клиента (должен быть валидным)
     */
    private String email;

    /**
     * пароль клиента (не возвращается из базы данных)
     */
    private String password;
}
