package by.stolybko.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@Builder
@EqualsAndHashCode(of = "id", callSuper = false)
public class UserEntity extends BaseEntity {

    private Long id;

    @NonNull
    private String fullName;

    @NonNull
    private String passportNumber;
    private String email;
    private String password;
}
