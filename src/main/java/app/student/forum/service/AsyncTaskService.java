package app.student.forum.service;

import app.student.forum.dto.async.AsyncTaskResponseDto;
import app.student.forum.entity.AsyncTask;
import app.student.forum.entity.Role;
import app.student.forum.entity.User;
import app.student.forum.exception.AccessDeniedException;
import app.student.forum.exception.ErrorCode;
import app.student.forum.exception.NotFoundException;
import app.student.forum.mapper.AsyncTaskMapper;
import app.student.forum.util.AsyncTaskExecutor;
import app.student.forum.util.AsyncTaskStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AsyncTaskService {


    private final AsyncTaskStorage asyncTaskStorage;
    private final AsyncTaskMapper asyncTaskMapper;
    private final AsyncTaskExecutor asyncTaskExecutor;

    public AsyncTaskResponseDto startTask(User user) {
        AsyncTask task = asyncTaskStorage.create(user);

        asyncTaskExecutor.executeTask(task.getId());

        return asyncTaskMapper.toDto(task);
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
