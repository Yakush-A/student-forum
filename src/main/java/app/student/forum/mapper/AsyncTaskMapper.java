package app.student.forum.mapper;

import app.student.forum.dto.async.AsyncTaskResponseDto;
import app.student.forum.entity.AsyncTask;
import org.springframework.stereotype.Component;

@Component
public class AsyncTaskMapper {

    public AsyncTaskResponseDto toDto(AsyncTask asyncTask) {
        AsyncTaskResponseDto dto = new AsyncTaskResponseDto();

        dto.setTaskStatus(asyncTask.getStatus());
        dto.setTaskId(asyncTask.getId());
        dto.setUserId(asyncTask.getUser().getId());

        return dto;
    }
}
