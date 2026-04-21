package app.student.forum.service;

import app.student.forum.dto.async.AsyncTaskResponseDto;
import app.student.forum.entity.AsyncTask;
import app.student.forum.entity.Role;
import app.student.forum.entity.TaskStatus;
import app.student.forum.entity.User;
import app.student.forum.exception.AccessDeniedException;
import app.student.forum.exception.NotFoundException;
import app.student.forum.mapper.AsyncTaskMapper;
import app.student.forum.util.AsyncTaskStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.*;

class AsyncTaskServiceTest {
}
