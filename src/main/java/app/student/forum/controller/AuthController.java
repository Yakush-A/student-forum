package app.student.forum.controller;

import app.student.forum.dto.auth.JwtResponseDto;
import app.student.forum.dto.auth.LoginRequestDto;
import app.student.forum.dto.auth.RegisterRequestDto;
import app.student.forum.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public JwtResponseDto login(@RequestBody LoginRequestDto loginRequestDto) {
        return authService.login(loginRequestDto);
    }

    @PostMapping("/register")
    public JwtResponseDto register(@RequestBody RegisterRequestDto registerRequestDto) {
        return authService.register(registerRequestDto);
    }
}
