package by.stolybko.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldNameConstants;

@Data
@Builder
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class UserEntity extends BaseEntity {

    private Long id;

    @NonNull
    private String fullName;

    @NonNull
    private String passportNumber;
    private String email;
    private String password;
}
