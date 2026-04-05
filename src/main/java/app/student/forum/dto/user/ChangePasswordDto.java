package app.student.forum.dto.user;

import app.student.forum.validation.ValidationConstants;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Запрос на изменение пароля пользователя")
public class ChangePasswordDto {

    @NotBlank
    @Schema(
            description = "Текущий пароль пользователя",
            example = "oldPassword123",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String oldPassword;

    @NotBlank
    @Size(
            min = ValidationConstants.MIN_NAME_LENGTH,
            max = ValidationConstants.MAX_NAME_LENGTH
    )
    @Schema(
            description = "Новый пароль пользователя",
            example = "newStrongPassword123",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String newPassword;
}
