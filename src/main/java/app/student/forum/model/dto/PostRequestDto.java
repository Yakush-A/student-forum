package app.student.forum.model.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class PostRequestDto {

    private Long authorId;
    private Long categoryId;

    private String content;

    private List<Long> tagIds;

}
