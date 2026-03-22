package app.student.forum.dto.post;

import app.student.forum.validation.ValidationConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class PostUpdateDto {
    @NotBlank(message = "{post.content.notBlank}")
    @Size(max = ValidationConstants.MAX_CONTENT_LENGTH, message = "{post.content.size}")
    private String content;

    @NotNull
    @Positive
    private Long categoryId;

    private Set<@NotNull @Positive Long> tagIds;

}
