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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Validated
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserDetailsResponseDto getById(Long id) {
        log.debug("Getting user with id {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toDetailsDto(user);
    }

    public Page<UserResponseDto> getAll(Pageable pageable) {
        log.debug("Getting all users");

        return userRepository.findAll(pageable).map(userMapper::toDto);
    }

    public void changePassword(@Valid ChangePasswordDto dto, User user) {
        log.info("Changing password for user with id {}", user.getId());

        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            log.warn("Passwords don't match for user {}", user.getId());
            throw new BadRequestException(ErrorCode.PASSWORD_MISMATCH);
        } else if (dto.getNewPassword().equals(dto.getOldPassword())) {
            log.warn("New password is the same as old for user {}", user.getId());
            throw new BadRequestException(ErrorCode.PASSWORD_SAME_AS_OLD);
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        log.info("Password changed for user with id {}", user.getId());
    }

    public void changeRole(Long id, @Valid ChangeRoleDto changeRoleDto) {
        log.info("Changing role for user with id {}", id);

        User user = userRepository.findWithPostsById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        user.setRole(changeRoleDto.getRole());
        log.info("Role changed for user with id {}", id);
        userRepository.save(user);
    }

    public void changeEmail(@Valid ChangeEmailDto changeEmailDto, User user) {
        log.info("Changing email for user with id {}", user.getId());
        user.setEmail(changeEmailDto.getEmail());
        log.info("Email changed for user with id {}", user.getId());
        userRepository.save(user);
    }

    public void changeUsername(@Valid ChangeUsernameDto changeUsernameDto, User user) {
        log.info("Changing username for user with id {}", user.getId());
        user.setUsername(changeUsernameDto.getUsername());
        log.info("Username changed for user with id {}", user.getId());
        userRepository.save(user);
    }

    public void deleteUserById(Long id, User user) {
        log.info("Deleting user with id {}", id);

        User userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        boolean isSameUser = user.getId().equals(userToDelete.getId());
        boolean isModerator = Role.MODERATOR.equals(user.getRole());
        boolean isAdmin = Role.ADMIN.equals(user.getRole());

        if (isSameUser || isModerator || isAdmin) {
            userRepository.delete(userToDelete);
            log.info("User with id {} has been deleted", id);
        } else {
            log.warn("User with id {} can not delete user with id {}", user.getId(), id);
            throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        }
    }
}
