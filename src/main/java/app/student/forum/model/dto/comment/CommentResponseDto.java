package app.student.forum.model.dto.comment;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentResponseDto {

    private Long id;
    private String content;

    private Long postId;
    private Long authorId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
