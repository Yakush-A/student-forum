package app.student.forum.service;

import app.student.forum.dto.user.*;
import app.student.forum.entity.Role;
import app.student.forum.entity.User;
import app.student.forum.exception.AccessDeniedException;
import app.student.forum.exception.BadRequestException;
import app.student.forum.exception.NotFoundException;
import app.student.forum.mapper.UserMapper;
import app.student.forum.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final Long USER_ID = 1L;
    private static final int PAGE_SIZE = 10;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void getByIdShouldReturnUser() {
        User user = new User();
        user.setId(USER_ID);

        UserDetailsResponseDto response = new UserDetailsResponseDto();

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(userMapper.toDetailsDto(user)).thenReturn(response);

        UserDetailsResponseDto result = userService.getById(USER_ID);

        assertEquals(response, result);

        verify(userRepository).findById(USER_ID);
        verify(userMapper).toDetailsDto(user);
    }

    @Test
    void getByIdShouldThrowNotFound() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> userService.getById(USER_ID));

        verify(userRepository).findById(USER_ID);
    }

    @Test
    void getAllShouldReturnPage() {
        Pageable pageable = PageRequest.of(0, PAGE_SIZE);

        User user = new User();
        Page<User> page = new PageImpl<>(List.of(user));

        UserResponseDto dto = new UserResponseDto();

        when(userRepository.findAll(pageable)).thenReturn(page);
        when(userMapper.toDto(user)).thenReturn(dto);

        Page<UserResponseDto> result = userService.getAll(pageable);

        assertEquals(1, result.getTotalElements());

        verify(userRepository).findAll(pageable);
        verify(userMapper).toDto(user);
    }

    @Test
    void changePasswordShouldChangePassword() {
        User user = new User();
        user.setId(USER_ID);
        user.setPassword("oldEncoded");

        ChangePasswordDto dto = new ChangePasswordDto("old", "new");

        when(passwordEncoder.matches("old", "oldEncoded")).thenReturn(true);
        when(passwordEncoder.encode("new")).thenReturn("newEncoded");

        userService.changePassword(dto, user);

        assertEquals("newEncoded", user.getPassword());

        verify(passwordEncoder).matches("old", "oldEncoded");
        verify(passwordEncoder).encode("new");
    }

    @Test
    void changePasswordShouldThrowMismatch() {
        User user = new User();
        user.setId(USER_ID);
        user.setPassword("oldEncoded");

        ChangePasswordDto dto = new ChangePasswordDto("wrong", "new");

        when(passwordEncoder.matches("wrong", "oldEncoded")).thenReturn(false);

        assertThrows(BadRequestException.class,
                () -> userService.changePassword(dto, user));

        verify(passwordEncoder).matches("wrong", "oldEncoded");
        verify(passwordEncoder, never()).encode(any());
    }

    @Test
    void changePasswordShouldThrowSamePassword() {
        User user = new User();
        user.setId(USER_ID);
        user.setPassword("oldEncoded");

        ChangePasswordDto dto = new ChangePasswordDto("old", "old");

        when(passwordEncoder.matches("old", "oldEncoded")).thenReturn(true);

        assertThrows(BadRequestException.class,
                () -> userService.changePassword(dto, user));

        verify(passwordEncoder).matches("old", "oldEncoded");
        verify(passwordEncoder, never()).encode(any());
    }

    @Test
    void changeRoleShouldUpdateRole() {
        User user = new User();
        user.setId(USER_ID);

        ChangeRoleDto dto = new ChangeRoleDto(Role.ADMIN);

        when(userRepository.findWithPostsById(USER_ID)).thenReturn(Optional.of(user));

        userService.changeRole(USER_ID, dto);

        assertEquals(Role.ADMIN, user.getRole());

        verify(userRepository).save(user);
    }

    @Test
    void changeRoleShouldThrowNotFound() {
        when(userRepository.findWithPostsById(USER_ID)).thenReturn(Optional.empty());

        ChangeRoleDto dto = new ChangeRoleDto(Role.ADMIN);

        assertThrows(NotFoundException.class,
                () -> userService.changeRole(USER_ID, dto));
    }

    @Test
    void changeEmailShouldUpdateEmail() {
        User user = new User();
        user.setId(USER_ID);

        ChangeEmailDto dto = new ChangeEmailDto("new@mail.com");

        userService.changeEmail(dto, user);

        assertEquals("new@mail.com", user.getEmail());

        verify(userRepository).save(user);
    }

    @Test
    void changeUsernameShouldUpdateUsername() {
        User user = new User();
        user.setId(USER_ID);

        ChangeUsernameDto dto = new ChangeUsernameDto("newName");

        userService.changeUsername(dto, user);

        assertEquals("newName", user.getUsername());

        verify(userRepository).save(user);
    }

    @Test
    void deleteUserShouldDeleteWhenSameUser() {
        User current = new User();
        current.setId(USER_ID);
        current.setRole(Role.USER);

        User target = new User();
        target.setId(USER_ID);

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(target));

        userService.deleteUserById(USER_ID, current);

        verify(userRepository).delete(target);
    }

    @Test
    void deleteUserShouldDeleteWhenAdmin() {
        User current = new User();
        current.setId(2L);
        current.setRole(Role.ADMIN);

        User target = new User();
        target.setId(USER_ID);

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(target));

        userService.deleteUserById(USER_ID, current);

        verify(userRepository).delete(target);
    }

    @Test
    void deleteUserShouldThrowAccessDenied() {
        User current = new User();
        current.setId(2L);
        current.setRole(Role.USER);

        User target = new User();
        target.setId(USER_ID);

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(target));

        assertThrows(AccessDeniedException.class,
                () -> userService.deleteUserById(USER_ID, current));

        verify(userRepository, never()).delete(any());
    }

    @Test
    void deleteUserShouldThrowNotFound() {
        User current = new User();
        current.setId(USER_ID);

        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> userService.deleteUserById(USER_ID, current));
    }
}
