package app.student.forum.dto.tag;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Тег")
public class TagResponseDto {

    @Schema(description = "ID тега", example = "1")
    private Long id;

    @Schema(description = "Название тега", example = "java")
    private String name;
}
