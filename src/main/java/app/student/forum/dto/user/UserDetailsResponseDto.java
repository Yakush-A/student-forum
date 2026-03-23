package app.student.forum.dto.user;

import app.student.forum.dto.post.PostResponseDto;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "Детальная информация о пользователе")
public class UserDetailsResponseDto {

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

    @ArraySchema(
            schema = @Schema(implementation = PostResponseDto.class),
            arraySchema = @Schema(description = "Посты пользователя")
    )
    private List<PostResponseDto> posts;
}
