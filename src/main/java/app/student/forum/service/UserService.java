package app.student.forum.service;

import app.student.forum.dto.user.*;
import app.student.forum.exception.AccessDeniedException;
import app.student.forum.exception.BadRequestException;
import app.student.forum.exception.ErrorCode;
import app.student.forum.exception.NotFoundException;
import app.student.forum.mapper.UserMapper;
import app.student.forum.entity.Role;
import app.student.forum.entity.User;
import app.student.forum.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Validated
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final Map<Long, UserDetailsResponseDto> userCache = new ConcurrentHashMap<>();

    public UserDetailsResponseDto getById(Long id) {

        return userCache.computeIfAbsent(
                id,
                k -> {
                    User user = userRepository.findById(id)
                            .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
                    return userMapper.toDetailsDto(user);
                }
        );
    }

    public Page<UserResponseDto> getAll(Pageable pageable) {

        return userRepository.findAll(pageable).map(userMapper::toDto);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void changePassword(@Valid ChangePasswordDto dto, User user) {

        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new BadRequestException(ErrorCode.PASSWORD_MISMATCH);
        } else if (dto.getNewPassword().equals(dto.getOldPassword())) {
            throw new BadRequestException(ErrorCode.PASSWORD_SAME_AS_OLD);
        }

        userCache.remove(user.getId());
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
    }

    public void changeRole(Long id, @Valid ChangeRoleDto changeRoleDto) {

        User user = userRepository.findWithPostsById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        user.setRole(changeRoleDto.getRole());
        userRepository.save(user);
        userCache.remove(user.getId());
    }

    public void changeEmail(@Valid ChangeEmailDto changeEmailDto, User user) {
        user.setEmail(changeEmailDto.getEmail());
        userRepository.save(user);
    }

    public void changeUsername(@Valid ChangeUsernameDto changeUsernameDto, User user) {
        user.setUsername(changeUsernameDto.getUsername());
        userRepository.save(user);
    }

    public void deleteUserById(Long id, User user) {

        User userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        boolean isSameUser = user.getId().equals(userToDelete.getId());
        boolean isModerator = user.getRole().equals(Role.MODERATOR);
        boolean isAdmin = user.getRole().equals(Role.ADMIN);

        if (isSameUser || isModerator || isAdmin) {
            userRepository.delete(userToDelete);
        } else {
            throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        }
    }
}
