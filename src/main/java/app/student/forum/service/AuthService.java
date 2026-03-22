package app.student.forum.service;

import app.student.forum.exception.ErrorCode;
import app.student.forum.dto.auth.JwtResponseDto;
import app.student.forum.dto.auth.LoginRequestDto;
import app.student.forum.dto.auth.RegisterRequestDto;
import app.student.forum.entity.Role;
import app.student.forum.entity.User;
import app.student.forum.exception.UnauthorizedException;
import app.student.forum.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

@Validated
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public JwtResponseDto login(@Valid LoginRequestDto loginRequestDto) {

        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new UnauthorizedException(ErrorCode.INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new UnauthorizedException(ErrorCode.INVALID_CREDENTIALS);
        }

        String token = jwtService.generateToken(user);

        return new JwtResponseDto(token);
    }

    public JwtResponseDto register(@Valid RegisterRequestDto registerRequestDto) {

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
