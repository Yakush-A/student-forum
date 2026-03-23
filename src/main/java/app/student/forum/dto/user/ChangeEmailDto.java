package app.student.forum.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Запрос на изменение email пользователя")
public class ChangeEmailDto {

    @NotBlank
    @Email
    @Schema(
            description = "Новый email пользователя",
            example = "new.email@example.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String email;
}
