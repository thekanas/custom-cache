package by.stolybko.mapper;

import by.stolybko.dto.UserRequestDTO;
import by.stolybko.dto.UserResponseDTO;
import by.stolybko.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserEntity toUserEntity(UserRequestDTO userRequestDTO);

    UserResponseDTO toUserResponseDTO(UserEntity userEntity);
}
