package app.student.forum.service;

import app.student.forum.mapper.UserMapper;
import app.student.forum.model.dto.UserDetailsResponseDto;
import app.student.forum.model.dto.UserRequestDto;
import app.student.forum.model.dto.UserResponseDto;
import app.student.forum.model.entity.Role;
import app.student.forum.model.entity.User;
import app.student.forum.repository.RoleRepository;
import app.student.forum.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final UserMapper userMapper;

    public UserDetailsResponseDto getById(Long id) {

        User user = userRepository.findWithPostsById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return userMapper.toDetailsDto(user);
    }

    public List<UserResponseDto> getAll() {

        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    public UserResponseDto createUser(UserRequestDto userRequestDto) {

        User user = userMapper.toEntity(userRequestDto);
        Role userRole = roleRepository.findByName("User")
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.setRole(userRole);

        User savedUser = userRepository.save(user);

        return userMapper.toDto(savedUser);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
