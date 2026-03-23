package app.student.forum.dto.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Schema(description = "Краткая информация о посте")
public class PostResponseDto {

    @Schema(description = "ID поста", example = "1")
    private Long id;

    @Schema(description = "ID автора", example = "7")
    private Long authorId;

    @Schema(description = "ID категории", example = "3")
    private Long categoryId;

    @Schema(description = "Содержимое поста", example = "Текст поста...")
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(
            description = "Дата создания",
            example = "2026-03-23 14:30:00",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(
            description = "Дата последнего редактирования",
            example = "2026-03-23 15:00:00",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private LocalDateTime editedAt;

    @ArraySchema(
            schema = @Schema(description = "ID тега", example = "1"),
            arraySchema = @Schema(description = "Список ID тегов")
    )
    private List<Long> tagIds;

    @ArraySchema(
            schema = @Schema(description = "ID комментария", example = "10"),
            arraySchema = @Schema(description = "Список ID комментариев")
    )
    private List<Long> commentIds;
}
