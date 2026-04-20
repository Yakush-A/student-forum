package app.student.forum.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AsyncTask {
    private User user;
    private String id;
    private TaskStatus status;

    public AsyncTask(String taskId, User user) {
        this.status = TaskStatus.PENDING;
        this.id = taskId;
        this.user = user;
    }
}
