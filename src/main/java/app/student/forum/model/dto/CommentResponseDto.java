package app.student.forum.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentResponseDto {

    private Long id;
    private String content;

    private Long postId;
    private Long authorId;

}
