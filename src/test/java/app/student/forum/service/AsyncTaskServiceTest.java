package app.student.forum.service;

import app.student.forum.dto.async.AsyncTaskResponseDto;
import app.student.forum.entity.AsyncTask;
import app.student.forum.entity.Role;
import app.student.forum.entity.TaskStatus;
import app.student.forum.entity.User;
import app.student.forum.exception.AccessDeniedException;
import app.student.forum.exception.NotFoundException;
import app.student.forum.mapper.AsyncTaskMapper;
import app.student.forum.util.AsyncTaskExecutor;
import app.student.forum.util.AsyncTaskStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AsyncTaskServiceTest {

    @Mock
    private AsyncTaskStorage storage;

    @Mock
    private AsyncTaskMapper mapper;

    @Mock
    private AsyncTaskExecutor executor;

    @InjectMocks
    private AsyncTaskService service;

    @Test
    void startTaskShouldCreateTaskAndExecuteAsync() {
        User user = new User();
        user.setId(1L);

        AsyncTask task = new AsyncTask("task-1", user);

        AsyncTaskResponseDto dto =
                new AsyncTaskResponseDto("task-1", 1L, TaskStatus.PENDING);

        when(storage.create(user)).thenReturn(task);
        when(mapper.toDto(task)).thenReturn(dto);

        AsyncTaskResponseDto result = service.startTask(user);

        assertEquals(dto, result);

        verify(storage).create(user);
        verify(executor).executeTask("task-1");
        verify(mapper).toDto(task);
    }

    @Test
    void getByIdShouldReturnTaskWhenSameUser() {
        User user = new User();
        user.setId(1L);
        user.setRole(Role.USER);

        AsyncTask task = new AsyncTask("task-1", user);

        AsyncTaskResponseDto dto =
                new AsyncTaskResponseDto("task-1", 1L, TaskStatus.PENDING);

        when(storage.get("task-1")).thenReturn(Optional.of(task));
        when(mapper.toDto(task)).thenReturn(dto);

        AsyncTaskResponseDto result = service.getById("task-1", user);

        assertEquals(dto, result);
    }

    @Test
    void getByIdShouldAllowAdmin() {
        User owner = new User();
        owner.setId(1L);

        User admin = new User();
        admin.setId(2L);
        admin.setRole(Role.ADMIN);

        AsyncTask task = new AsyncTask("task-1", owner);

        when(storage.get("task-1")).thenReturn(Optional.of(task));
        when(mapper.toDto(task)).thenReturn(
                new AsyncTaskResponseDto("task-1", 1L, TaskStatus.PENDING)
        );

        AsyncTaskResponseDto result = service.getById("task-1", admin);

        assertNotNull(result);
    }

    @Test
    void getByIdShouldAllowModerator() {
        User owner = new User();
        owner.setId(1L);

        User moderator = new User();
        moderator.setId(2L);
        moderator.setRole(Role.MODERATOR);

        AsyncTask task = new AsyncTask("task-1", owner);

        when(storage.get("task-1")).thenReturn(Optional.of(task));
        when(mapper.toDto(task)).thenReturn(
                new AsyncTaskResponseDto("task-1", 1L, TaskStatus.PENDING)
        );

        AsyncTaskResponseDto result = service.getById("task-1", moderator);

        assertNotNull(result);
    }

    @Test
    void getByIdShouldThrowAccessDeniedWhenDifferentUser() {
        User owner = new User();
        owner.setId(1L);
        owner.setRole(Role.USER);

        User another = new User();
        another.setId(2L);
        another.setRole(Role.USER);

        AsyncTask task = new AsyncTask("task-1", owner);

        when(storage.get("task-1")).thenReturn(Optional.of(task));

        assertThrows(AccessDeniedException.class,
                () -> service.getById("task-1", another));
    }

    @Test
    void getByIdShouldThrowNotFoundWhenTaskMissing() {
        User user = new User();
        user.setId(1L);

        when(storage.get("task-1")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.getById("task-1", user));
    }

}
