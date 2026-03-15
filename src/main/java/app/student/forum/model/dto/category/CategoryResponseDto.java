package app.student.forum.model.dto.category;

import app.student.forum.model.dto.post.PostResponseDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CategoryResponseDto {
    private Long id;
    private String name;
    private String description;

    private List<PostResponseDto> posts;
}
