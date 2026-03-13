package app.student.forum.controller;

import app.student.forum.model.dto.JwtResponseDto;
import app.student.forum.model.dto.LoginRequestDto;
import app.student.forum.model.dto.RegisterRequestDto;
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
