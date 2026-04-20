package app.student.forum.service;

import app.student.forum.dto.async.AsyncTaskResponseDto;
import app.student.forum.entity.AsyncTask;
import app.student.forum.entity.Role;
import app.student.forum.entity.TaskStatus;
import app.student.forum.entity.User;
import app.student.forum.exception.AccessDeniedException;
import app.student.forum.exception.ErrorCode;
import app.student.forum.exception.NotFoundException;
import app.student.forum.mapper.AsyncTaskMapper;
import app.student.forum.util.AsyncTaskStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Slf4j
@EnableAsync
@RequiredArgsConstructor
@Service
public class AsyncTaskService {

    public static final int TASK_EXECUTION_TIME = 10000;

    private final AsyncTaskStorage asyncTaskStorage;
    private final AsyncTaskMapper asyncTaskMapper;

    public String startTask(User user) {
        String taskId = asyncTaskStorage.create(user);
        log.info("User {} started task {}", user.getId(), taskId);

        executeTask(taskId);

        return taskId;
    }

    @Async
    public CompletableFuture<Void> executeTask(String taskId) {
        log.info("Task {} started", taskId);

        return CompletableFuture.runAsync(() -> {
            try {
                log.debug("Task {} executing", taskId);
                asyncTaskStorage.updateTask(taskId, TaskStatus.IN_PROGRESS);

                Thread.sleep(TASK_EXECUTION_TIME);

                asyncTaskStorage.updateTask(taskId, TaskStatus.DONE);
                log.debug("Task {} finished successfully", taskId);

            } catch (Exception ex) {

                log.error("Error! Task {} interrupted", taskId);
                asyncTaskStorage.updateTask(taskId, TaskStatus.FAILED);
            }
        });
    }

    public AsyncTaskResponseDto getById(String taskId, User user) {
        log.debug("User {} trying to get {} task info", user.getId(), taskId);

        AsyncTask task = asyncTaskStorage.get(taskId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.TASK_NOT_FOUND));

        boolean isSameUser = task.getUser().getId().equals(user.getId());
        boolean isModerator = Role.MODERATOR.equals(user.getRole());
        boolean isAdmin = Role.ADMIN.equals(user.getRole());

        if (isSameUser || isModerator || isAdmin) {
            log.debug("User {} watching {} task info", user.getId(), taskId);

            return asyncTaskMapper.toDto(task);

        } else {
            log.warn("Access denied! User {} can not see {} task info", user.getId(), taskId);
            throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        }

    }

}
