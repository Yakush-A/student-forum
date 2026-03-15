package app.student.forum.model.dto.post;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostRequestDto {

    private Long categoryId;
    private String content;
    private List<Long> tagIds;

}
