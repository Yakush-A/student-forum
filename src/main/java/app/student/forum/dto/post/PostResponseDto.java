package app.student.forum.dto.post;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PostResponseDto {

    private Long id;

    private Long authorId;
    private Long categoryId;

    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime editedAt;

    private List<Long> tagIds;
    private List<Long> commentIds;
}
