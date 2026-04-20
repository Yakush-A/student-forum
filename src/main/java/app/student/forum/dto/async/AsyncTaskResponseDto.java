package app.student.forum.dto.async;

import app.student.forum.entity.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Ответ запроса состояния асинхронной бизнес задачи")
public class AsyncTaskResponseDto {
    @Schema(
            description = "b",
            example = "a"
    )
    private String taskId;

    @Schema(
            description = "b",
            example = "a"
    )
    private Long userId;

    @Schema(
            description = "b",
            example = "a"
    )
    private TaskStatus taskStatus;

}
