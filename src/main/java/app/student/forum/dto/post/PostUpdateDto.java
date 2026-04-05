package app.student.forum.dto.post;

import app.student.forum.validation.ValidationConstants;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Запрос на обновление поста")
public class PostUpdateDto {

    @NotBlank(message = "{post.content.notBlank}")
    @Size(
            max = ValidationConstants.MAX_CONTENT_LENGTH,
            message = "{post.content.size}"
    )
    @Schema(
            description = "Обновлённый текст поста",
            example = "Обновлённый текст поста...",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String content;

    @NotNull
    @Positive
    @Schema(
            description = "ID новой категории",
            example = "3",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long categoryId;

    @ArraySchema(
            schema = @Schema(description = "ID тега", example = "1"),
            arraySchema = @Schema(description = "Новый набор тегов (заменяет старые)")
    )
    private Set<@NotNull @Positive Long> tagIds;
}
