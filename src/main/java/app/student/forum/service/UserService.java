package app.student.forum.service;

import app.student.forum.mapper.UserMapper;
import app.student.forum.model.dto.user.*;
import app.student.forum.model.entity.Role;
import app.student.forum.model.entity.User;
import app.student.forum.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

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

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void changePassword(ChangePasswordDto changePasswordDto, User user) {
        if (!passwordEncoder.encode(changePasswordDto.getOldPassword()).equals(user.getPassword())) {
            throw new RuntimeException("Old Password Doesn't Match");
        }

        if (changePasswordDto.getNewPassword().equals(changePasswordDto.getNewPassword())) {
            throw new RuntimeException("New Password is the same");
        }

        user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
    }

    public void changeRole(Long id, ChangeRoleDto changeRoleDto) {

        User user = userRepository.findWithPostsById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setRole(changeRoleDto.getRole());
        userRepository.save(user);
    }

    public void changeEmail(ChangeEmailDto changeEmailDto, User user) {
        user.setEmail(changeEmailDto.getEmail());
        userRepository.save(user);
    }

    public void changeUsername(ChangeUsernameDto changeUsernameDto, User user) {
        user.setUsername(changeUsernameDto.getUsername());
        userRepository.save(user);
    }

    public void deleteUserById(Long id, User user) {

        User userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isSameUser = user.getId().equals(userToDelete.getId());
        boolean isModerator = user.getRole().equals(Role.MODERATOR);
        boolean isAdmin = user.getRole().equals(Role.ADMIN);

        if (isSameUser || isModerator || isAdmin) {
            userRepository.delete(userToDelete);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }
}
