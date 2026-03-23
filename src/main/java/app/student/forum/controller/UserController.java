package app.student.forum.controller;

import app.student.forum.dto.user.*;
import app.student.forum.security.CustomUserDetails;
import app.student.forum.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Управление пользователями и профилем")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Получить пользователя по ID",
            description = "Возвращает детальную информацию о пользователе"
    )
    @GetMapping("/{id}")
    public UserDetailsResponseDto getUser(

            @Parameter(description = "ID пользователя", example = "1")
            @PathVariable Long id
    ) {
        return userService.getById(id);
    }

    @Operation(
            summary = "Получить список пользователей",
            description = "Возвращает список пользователей с пагинацией"
    )
    @GetMapping
    public Page<UserResponseDto> getAllUsers(
            @Parameter(hidden = true) Pageable pageable
    ) {
        return userService.getAll(pageable);
    }

    @Operation(
            summary = "Удалить пользователя",
            description = "Удаляет пользователя (сам себя или администратор)"
    )
    @DeleteMapping("/{id}")
    public void deleteUser(

            @Parameter(description = "ID пользователя", example = "1")
            @PathVariable Long id,

            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        userService.deleteUserById(id, customUserDetails.getUser());
    }

    @Operation(
            summary = "Изменить email",
            description = "Обновляет email текущего пользователя"
    )
    @PatchMapping("/email")
    public void updateEmail(

            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Новый email",
                    required = true
            )
            @RequestBody ChangeEmailDto changeEmailDto,

            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        userService.changeEmail(changeEmailDto, customUserDetails.getUser());
    }

    @Operation(
            summary = "Изменить username",
            description = "Обновляет имя пользователя"
    )
    @PatchMapping("/username")
    public void updateUsername(

            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Новое имя пользователя",
                    required = true
            )
            @RequestBody ChangeUsernameDto changeUsernameDto,

            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        userService.changeUsername(changeUsernameDto, customUserDetails.getUser());
    }

    @Operation(
            summary = "Изменить пароль",
            description = "Обновляет пароль текущего пользователя"
    )
    @PatchMapping("/password")
    public void updatePassword(

            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для смены пароля",
                    required = true
            )
            @RequestBody ChangePasswordDto changePasswordDto,

            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        userService.changePassword(changePasswordDto, customUserDetails.getUser());
    }

    @Operation(
            summary = "Изменить роль пользователя",
            description = "Изменяет роль пользователя (доступно только ADMIN)"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/role")
    public void updateRole(

            @Parameter(description = "ID пользователя", example = "1")
            @PathVariable Long id,

            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Новая роль",
                    required = true
            )
            @RequestBody ChangeRoleDto changeRoleDto
    ) {
        userService.changeRole(id, changeRoleDto);
    }
}
