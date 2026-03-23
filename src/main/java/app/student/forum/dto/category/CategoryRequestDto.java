package app.student.forum.dto.category;

import app.student.forum.validation.ValidationConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Запрос на создание или обновление категории")
public class CategoryRequestDto {

    @NotBlank
    @Size(
            min = ValidationConstants.MIN_CATEGORY_LENGTH,
            max = ValidationConstants.MAX_CATEGORY_LENGTH
    )
    @Schema(
            description = "Название категории",
            example = "Technology",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String name;

    @Size(
            max = ValidationConstants.MAX_DESCRIPTION_LENGTH
    )
    @Schema(
            description = "Описание категории",
            example = "Категория для технических статей"
    )
    private String description;
}
