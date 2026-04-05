package app.student.forum.dto.auth;

import app.student.forum.validation.ValidationConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Запрос на регистрацию пользователя")
public class RegisterRequestDto {

    @NotBlank
    @Size(
            min = ValidationConstants.MIN_PASSWORD_LENGTH,
            max = ValidationConstants.MAX_PASSWORD_LENGTH
    )
    @Schema(
            description = "Имя пользователя (уникальное)",
            example = "cool_user",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String username;

    @NotBlank
    @Size(
            min = ValidationConstants.MIN_PASSWORD_LENGTH,
            max = ValidationConstants.MAX_PASSWORD_LENGTH
    )
    @Schema(
            description = "Пароль пользователя",
            example = "strongPassword123",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String password;

    @NotBlank
    @Email
    @Schema(
            description = "Email пользователя",
            example = "user@example.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String email;

}
