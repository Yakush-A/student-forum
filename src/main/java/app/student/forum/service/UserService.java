package app.student.forum.service;

import app.student.forum.exception.BadRequestException;
import app.student.forum.exception.ForbiddenAccessException;
import app.student.forum.exception.NotFoundException;
import app.student.forum.mapper.UserMapper;
import app.student.forum.model.dto.user.*;
import app.student.forum.model.entity.Role;
import app.student.forum.model.entity.User;
import app.student.forum.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final String USER_NOT_FOUND = "User not found";

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final Map<Long, UserDetailsResponseDto> userCache = new ConcurrentHashMap<>();

    public UserDetailsResponseDto getById(Long id) {

        return userCache.computeIfAbsent(
                id,
                k -> {
                    User user = userRepository.findById(id)
                            .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
                    return userMapper.toDetailsDto(user);
                }
        );
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
            throw new BadRequestException("Old password doesn't match");
        } else if (dto.getNewPassword().equals(dto.getOldPassword())) {
            throw new BadRequestException("New password must be different");
        }

        userCache.remove(user.getId());
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
    }

    public void changeRole(Long id, ChangeRoleDto changeRoleDto) {

        User user = userRepository.findWithPostsById(id)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

        user.setRole(changeRoleDto.getRole());
        userRepository.save(user);
        userCache.remove(user.getId());
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
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

        boolean isSameUser = user.getId().equals(userToDelete.getId());
        boolean isModerator = user.getRole().equals(Role.MODERATOR);
        boolean isAdmin = user.getRole().equals(Role.ADMIN);

        if (isSameUser || isModerator || isAdmin) {
            userRepository.delete(userToDelete);
        } else {
            throw new ForbiddenAccessException("You are not allowed to delete this user");
        }
    }
}
