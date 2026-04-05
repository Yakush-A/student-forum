package app.student.forum.dto.comment;

import app.student.forum.validation.ValidationConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Запрос на создание или обновление комментария")
public class CommentRequestDto {

    @NotBlank
    @Size(
            max = ValidationConstants.MAX_CONTENT_LENGTH
    )
    @Schema(
            description = "Текст комментария",
            example = "Очень полезный пост!",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String content;

    @NotNull
    @Schema(
            description = "ID поста",
            example = "42",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long postId;
}
