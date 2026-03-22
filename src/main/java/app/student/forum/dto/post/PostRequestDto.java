package app.student.forum.dto.post;

import app.student.forum.validation.ValidationConstants;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostRequestDto {

    @NotNull
    @Positive
    private Long categoryId;

    @NotNull(message = "{post.content.notBlanc}")
    @Size(max = ValidationConstants.MAX_CONTENT_LENGTH, message = "{post.content.size}")
    private String content;

    private List<@NotNull @Positive Long> tagIds;

}
