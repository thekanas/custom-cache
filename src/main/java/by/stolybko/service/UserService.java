package by.stolybko.service;

import by.stolybko.dto.UserRequestDTO;
import by.stolybko.dto.UserResponseDTO;

import java.util.List;

public interface UserService {

    UserResponseDTO getUserById(Long id);
    List<UserResponseDTO> getAll();
    UserResponseDTO save(UserRequestDTO user);
    UserResponseDTO update(UserRequestDTO userDTO, Long id);
    void delete(Long id);
}
