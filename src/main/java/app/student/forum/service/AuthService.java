package app.student.forum.service;

import app.student.forum.model.dto.JwtResponseDto;
import app.student.forum.model.dto.LoginRequestDto;
import app.student.forum.model.dto.RegisterRequestDto;
import app.student.forum.model.entity.User;
import app.student.forum.repository.UserRepository;
import app.student.forum.security.JwtService;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public JwtResponseDto login(LoginRequestDto loginRequestDto) {

        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        String token = JwtService.generateToken(user);

        return new JwtResponseDto(token);
    }

    public JwtResponseDto register(RegisterRequestDto registerRequestDto) {

    }
}
