package app.student.forum.dto.user;

import app.student.forum.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Запрос на изменение роли пользователя")
public class ChangeRoleDto {

    @NotNull
    @Schema(
            description = "Новая роль пользователя",
            example = "USER",
            allowableValues = {"USER", "MODERATOR", "ADMIN"},
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Role role;
}
