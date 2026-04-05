package app.student.forum.dto.user;

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
@Schema(description = "Запрос на создание пользователя")
public class UserRequestDto {

    @NotBlank
    @Size(
            min = ValidationConstants.MIN_NAME_LENGTH,
            max = ValidationConstants.MAX_NAME_LENGTH,
            message = "{user.name.size}"
    )
    @Schema(
            description = "Имя пользователя",
            example = "alex_dev",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String username;

    @NotBlank
    @Size(
            min = ValidationConstants.MIN_PASSWORD_LENGTH,
            max = ValidationConstants.MAX_PASSWORD_LENGTH,
            message = "{user.password.size}"
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
