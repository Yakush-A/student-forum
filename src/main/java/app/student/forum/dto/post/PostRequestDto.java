package app.student.forum.dto.post;

import app.student.forum.validation.ValidationConstants;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "Запрос на создание или обновление поста")
public class PostRequestDto {

    @NotNull
    @Positive
    @Schema(
            description = "ID категории",
            example = "3",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long categoryId;

    @NotNull(message = "{post.content.notBlank}")
    @Size(
            max = ValidationConstants.MAX_CONTENT_LENGTH,
            message = "{post.content.size}"
    )
    @Schema(
            description = "Содержимое поста",
            example = "Текст поста...",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String content;

    @ArraySchema(
            schema = @Schema(description = "ID тега", example = "1"),
            arraySchema = @Schema(description = "Список ID тегов")
    )
    private List<@NotNull @Positive Long> tagIds;
}
