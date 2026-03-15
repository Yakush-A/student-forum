package app.student.forum.model.dto.post;

import app.student.forum.model.dto.comment.CommentResponseDto;
import app.student.forum.model.dto.tag.TagResponseDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PostDetailsResponseDto {
    private Long id;

    private Long authorId;
    private Long categoryId;

    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime editedAt;

    private List<CommentResponseDto> comments;
    private List<TagResponseDto> tags;
}
