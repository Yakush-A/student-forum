package app.student.forum.model.dto.post;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class PostUpdateDto {

    private String content;
    private Long categoryId;
    private Set<Long> tagIds;

}
