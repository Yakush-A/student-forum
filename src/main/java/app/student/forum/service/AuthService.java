package app.student.forum.service;

import app.student.forum.exception.UserNotFoundException;
import app.student.forum.model.dto.auth.JwtResponseDto;
import app.student.forum.model.dto.auth.LoginRequestDto;
import app.student.forum.model.dto.auth.RegisterRequestDto;
import app.student.forum.model.entity.Role;
import app.student.forum.model.entity.User;
import app.student.forum.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public JwtResponseDto login(LoginRequestDto loginRequestDto) {

        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Wrong password");
        }

        String token = jwtService.generateToken(user);

        return new JwtResponseDto(token);
    }

    public JwtResponseDto register(RegisterRequestDto registerRequestDto) {

        if (userRepository.findByEmail(registerRequestDto.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
        }

        User user = new User();
        user.setEmail(registerRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
        user.setRole(Role.USER);
        user.setUsername(registerRequestDto.getUsername());

        userRepository.save(user);

        String token = jwtService.generateToken(user);

        return new JwtResponseDto(token);
    }
}
