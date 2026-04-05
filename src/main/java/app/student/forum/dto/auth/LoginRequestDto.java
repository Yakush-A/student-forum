package app.student.forum.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на аутентификацию пользователя")
public class LoginRequestDto {

    @NotBlank
    @Email
    @Schema(
            description = "Email пользователя",
            example = "user@example.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String email;

    @NotBlank
    @Schema(
            description = "Пароль пользователя",
            example = "strongPassword123",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String password;

}
