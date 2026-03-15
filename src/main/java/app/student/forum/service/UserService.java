package app.student.forum.service;

import app.student.forum.exception.UserNotFoundException;
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
                .orElseThrow(UserNotFoundException::new);

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

    public void changePassword(ChangePasswordDto dto, User user) {

        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Old password doesn't match");
        }

        if (dto.getNewPassword().equals(dto.getOldPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New password must be different");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
    }

    public void changeRole(Long id, ChangeRoleDto changeRoleDto) {

        User user = userRepository.findWithPostsById(id)
                .orElseThrow(UserNotFoundException::new);

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
                .orElseThrow(UserNotFoundException::new);

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
