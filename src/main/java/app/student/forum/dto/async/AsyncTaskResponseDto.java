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
@Schema(description = "Ответ с информацией о состоянии асинхронной задачи")
public class AsyncTaskResponseDto {

    @Schema(
            description = "Уникальный идентификатор асинхронной задачи",
            example = "task-12345"
    )
    private String taskId;

    @Schema(
            description = "Идентификатор пользователя, инициировавшего задачу",
            example = "42"
    )
    private Long userId;

    @Schema(
            description = "Текущий статус выполнения задачи",
            example = "RUNNING",
            implementation = TaskStatus.class
    )
    private TaskStatus taskStatus;
}
