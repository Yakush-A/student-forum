package app.student.forum.util;

import app.student.forum.entity.AsyncTask;
import app.student.forum.entity.TaskStatus;
import app.student.forum.entity.User;
import app.student.forum.exception.ErrorCode;
import app.student.forum.exception.NotFoundException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AsyncTaskStorage {

    private final Map<String, AsyncTask> tasks = new ConcurrentHashMap<>();

    public AsyncTask create(User user) {

        String taskId = UUID.randomUUID().toString();

        AsyncTask asyncTask = new AsyncTask(taskId, user);

        tasks.put(taskId, asyncTask);

        return tasks.get(taskId);
    }

    public Optional<AsyncTask> get(String taskId) {
        return Optional.ofNullable(tasks.get(taskId));
    }

    public void updateTask(String taskId, TaskStatus status) {

        AsyncTask asyncTask = tasks.get(taskId);

        if (asyncTask == null) {
            throw new NotFoundException(ErrorCode.TASK_NOT_FOUND);
        }

        asyncTask.setStatus(status);
        tasks.put(taskId, asyncTask);
    }
}
