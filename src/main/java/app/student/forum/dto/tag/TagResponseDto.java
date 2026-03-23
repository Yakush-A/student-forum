package app.student.forum.dto.tag;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Тег")
public class TagResponseDto {

    @Schema(description = "ID тега", example = "1")
    private Long id;

    @Schema(description = "Название тега", example = "java")
    private String name;
}
