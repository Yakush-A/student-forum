package app.student.forum.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Краткая информация о пользователе")
public class UserResponseDto {

    @Schema(description = "ID пользователя", example = "1")
    private Long id;

    @Schema(description = "Имя пользователя", example = "alex_dev")
    private String username;

    @Schema(
            description = "Роль пользователя",
            example = "USER",
            allowableValues = {"USER", "MODERATOR", "ADMIN"}
    )
    private String role;
}
