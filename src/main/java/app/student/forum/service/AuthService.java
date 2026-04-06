package app.student.forum.service;

import app.student.forum.exception.ConflictException;
import app.student.forum.exception.ErrorCode;
import app.student.forum.dto.auth.JwtResponseDto;
import app.student.forum.dto.auth.LoginRequestDto;
import app.student.forum.dto.auth.RegisterRequestDto;
import app.student.forum.entity.Role;
import app.student.forum.entity.User;
import app.student.forum.exception.UnauthorizedException;
import app.student.forum.repository.UserRepository;
import app.student.forum.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Validated
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public JwtResponseDto login(@Valid LoginRequestDto loginRequestDto) {

        log.info("Login request received for email: {}", loginRequestDto.getEmail());

        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> {
                    log.warn("Login failed! User not found for email: {}", loginRequestDto.getEmail());
                    return new UnauthorizedException(ErrorCode.INVALID_CREDENTIALS);
                });

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            log.warn("Login failed! Invalid credentials for email: {}", loginRequestDto.getEmail());
            throw new UnauthorizedException(ErrorCode.INVALID_CREDENTIALS);
        }

        String token = jwtService.generateToken(user);

        log.info("Login success for email: {}", loginRequestDto.getEmail());
        return new JwtResponseDto(token);
    }

    public JwtResponseDto register(@Valid RegisterRequestDto registerRequestDto) {
        log.info("Register request received for email: {}", registerRequestDto.getEmail());

        if (userRepository.findByEmail(registerRequestDto.getEmail()).isPresent()) {
            log.warn("Registration failed! User already exists for email: {}", registerRequestDto.getEmail());
            throw new ConflictException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        User user = new User();
        user.setEmail(registerRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
        user.setRole(Role.USER);
        user.setUsername(registerRequestDto.getUsername());

        userRepository.save(user);

        String token = jwtService.generateToken(user);

        log.info("Register success for email: {}", registerRequestDto.getEmail());
        return new JwtResponseDto(token);
    }
}
