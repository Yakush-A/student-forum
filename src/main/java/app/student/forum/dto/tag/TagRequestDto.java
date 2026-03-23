package app.student.forum.dto.tag;

import app.student.forum.validation.ValidationConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Запрос на создание или обновление тега")
public class TagRequestDto {

    @NotBlank
    @Size(
            min = ValidationConstants.MIN_TAGS_LENGTH,
            max = ValidationConstants.MAX_TAGS_LENGTH
    )
    @Schema(
            description = "Название тега",
            example = "java",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String name;
}
