package app.student.forum.dto.user;

import app.student.forum.validation.ValidationConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Запрос на изменение имени пользователя")
public class ChangeUsernameDto {

    @NotBlank
    @Size(
            min = ValidationConstants.MIN_NAME_LENGTH,
            max = ValidationConstants.MAX_NAME_LENGTH,
            message = "{user.name.size}"
    )
    @Schema(
            description = "Новое имя пользователя",
            example = "new_cool_name",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String username;
}
