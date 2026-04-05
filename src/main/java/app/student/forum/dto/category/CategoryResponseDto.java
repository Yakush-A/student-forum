package app.student.forum.dto.category;

import app.student.forum.dto.post.PostResponseDto;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Категория с постами")
public class CategoryResponseDto {

    @Schema(description = "ID категории", example = "1")
    private Long id;

    @Schema(description = "Название категории", example = "Technology")
    private String name;

    @Schema(description = "Описание категории", example = "Категория для технических статей")
    private String description;

    @ArraySchema(
            schema = @Schema(implementation = PostResponseDto.class),
            arraySchema = @Schema(description = "Список постов в категории")
    )
    private List<PostResponseDto> posts;
}
