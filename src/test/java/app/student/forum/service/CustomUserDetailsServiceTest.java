package app.student.forum.service;

import app.student.forum.entity.User;
import app.student.forum.exception.NotFoundException;
import app.student.forum.repository.UserRepository;
import app.student.forum.security.CustomUserDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void loadUserByUsernameShouldReturnCustomUserDetails() {
        String email = "test@mail.com";

        User user = new User();
        user.setId(1L);
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        CustomUserDetails result =
                customUserDetailsService.loadUserByUsername(email);

        assertEquals(user, result.getUser());

        verify(userRepository).findByEmail(email);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void loadUserByUsernameShouldThrowNotFound() {
        String email = "test@mail.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername(email));

        verify(userRepository).findByEmail(email);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void loadUserByUsernameShouldReturnUser() {
        String email = "test@mail.com";

        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        CustomUserDetails result =
                customUserDetailsService.loadUserByUsername(email);

        assertEquals(user, result.getUser());
    }

    @Test
    void loadUserByUsernameShouldThrowWhenEmailIsNull() {
        assertThrows(NullPointerException.class,
                () -> customUserDetailsService.loadUserByUsername(null));

        verifyNoInteractions(userRepository);
    }
}
