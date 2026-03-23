package app.student.forum.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "Комментарий")
public class CommentResponseDto {

    @Schema(description = "ID комментария", example = "1")
    private Long id;

    @Schema(description = "Текст комментария", example = "Очень полезный пост!")
    private String content;

    @Schema(description = "ID поста", example = "42")
    private Long postId;

    @Schema(description = "ID автора комментария", example = "7")
    private Long authorId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(
            description = "Дата создания",
            example = "2026-03-23 14:30:00"
    )
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(
            description = "Дата последнего обновления",
            example = "2026-03-23 15:00:00"
    )
    private LocalDateTime updatedAt;
}

