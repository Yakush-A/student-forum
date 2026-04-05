package app.student.forum.service;

import app.student.forum.dto.auth.JwtResponseDto;
import app.student.forum.dto.auth.LoginRequestDto;
import app.student.forum.dto.auth.RegisterRequestDto;
import app.student.forum.entity.User;
import app.student.forum.exception.ConflictException;
import app.student.forum.exception.UnauthorizedException;
import app.student.forum.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    void loginShouldReturnToken() {
        LoginRequestDto dto = new LoginRequestDto("test@mail.com", "password");

        User user = new User();
        user.setEmail("test@mail.com");
        user.setPassword("encoded");

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encoded")).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("token");

        JwtResponseDto result = authService.login(dto);

        assertEquals("token", result.getToken());

        verify(userRepository).findByEmail("test@mail.com");
        verify(passwordEncoder).matches("password", "encoded");
        verify(jwtService).generateToken(user);
    }

    @Test
    void loginShouldThrowUnauthorizedWhenUserNotFound() {
        LoginRequestDto dto = new LoginRequestDto("test@mail.com", "password");

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.empty());

        assertThrows(UnauthorizedException.class, () -> authService.login(dto));

        verify(userRepository).findByEmail("test@mail.com");
        verifyNoMoreInteractions(passwordEncoder, jwtService);
    }

    @Test
    void loginShouldThrowUnauthorizedWhenPasswordInvalid() {
        LoginRequestDto dto = new LoginRequestDto("test@mail.com", "password");

        User user = new User();
        user.setEmail("test@mail.com");
        user.setPassword("encoded");

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encoded")).thenReturn(false);

        assertThrows(UnauthorizedException.class, () -> authService.login(dto));

        verify(passwordEncoder).matches("password", "encoded");
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void registerShouldCreateUserAndReturnToken() {
        RegisterRequestDto dto =
                new RegisterRequestDto("username", "password", "test@mail.com");

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encoded");
        when(jwtService.generateToken(any(User.class))).thenReturn("token");

        JwtResponseDto result = authService.register(dto);

        assertEquals("token", result.getToken());

        verify(userRepository).findByEmail("test@mail.com");
        verify(passwordEncoder).encode("password");
        verify(userRepository).save(any(User.class));
        verify(jwtService).generateToken(any(User.class));
    }

    @Test
    void registerShouldThrowConflictWhenUserExists() {
        RegisterRequestDto dto =
                new RegisterRequestDto("username", "password", "test@mail.com");

        when(userRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.of(new User()));

        assertThrows(ConflictException.class,
                () -> authService.register(dto));

        verify(userRepository).findByEmail("test@mail.com");
        verifyNoMoreInteractions(passwordEncoder, jwtService);
    }
}
