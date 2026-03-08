package app.student.forum.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDto {

    private Long id;
    private String text;

    private Long postId;
    private Long authorId;

}
