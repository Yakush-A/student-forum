package app.student.forum.controller;

import app.student.forum.dto.auth.JwtResponseDto;
import app.student.forum.dto.auth.LoginRequestDto;
import app.student.forum.dto.auth.RegisterRequestDto;
import app.student.forum.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Аутентификация и регистрация пользователей")
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Вход в систему",
            description = "Аутентифицирует пользователя и возвращает JWT токен"
    )
    @PostMapping("/login")
    public JwtResponseDto login(
            @Valid
            @RequestBody(required = true)
            LoginRequestDto loginRequestDto
    ) {
        return authService.login(loginRequestDto);
    }

    @Operation(
            summary = "Регистрация пользователя",
            description = "Создает нового пользователя и возвращает JWT токен"
    )
    @PostMapping("/register")
    public JwtResponseDto register(
            @Valid
            @RequestBody(required = true)
            RegisterRequestDto registerRequestDto
    ) {
        return authService.register(registerRequestDto);
    }
}
