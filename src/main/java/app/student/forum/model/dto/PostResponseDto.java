package app.student.forum.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class PostResponseDto {

    private Long id;

    private Long authorId;
    private Long categoryId;

    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime editedAt;

    private Set<Long> tagIds;

}
