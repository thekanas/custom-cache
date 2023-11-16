package by.stolybko.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(of = "id")
public class UserEntity implements BaseEntity<Long> {

    private Long id;
    private String fullName;
    private String passportNumber;
    private String email;
    private String password;
}
