package app.student.forum.controller;

import app.student.forum.dto.async.AsyncTaskResponseDto;
import app.student.forum.security.CustomUserDetails;
import app.student.forum.service.AsyncTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/tasks")
@Tag(name = "Асинхронные задачи", description = "API для запуска и отслеживания асинхронных задач")
public class AsyncTaskController {

    private final AsyncTaskService asyncTaskService;

    @Operation(
            summary = "Запуск асинхронной задачи",
            description = "Создает и запускает новую асинхронную задачу для текущего пользователя"
    )
    @PostMapping
    public AsyncTaskResponseDto startTask(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return asyncTaskService.startTask(user.getUser());
    }

    @Operation(
            summary = "Получить статус задачи",
            description = "Возвращает текущее состояние асинхронной задачи по её ID"
    )
    @GetMapping("/{id}")
    public AsyncTaskResponseDto getStatus(
            @Parameter(
                    description = "Идентификатор задачи",
                    example = "task-12345"
            )
            @PathVariable String id,

            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return asyncTaskService.getById(id, user.getUser());
    }
}
