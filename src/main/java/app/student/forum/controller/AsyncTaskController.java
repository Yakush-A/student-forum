package app.student.forum.controller;

import app.student.forum.dto.async.AsyncTaskResponseDto;
import app.student.forum.security.CustomUserDetails;
import app.student.forum.service.AsyncTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/tasks")
public class AsyncTaskController {

    private final AsyncTaskService asyncTaskService;

    @PostMapping
    public String startTask(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return asyncTaskService.startTask(user.getUser());
    }

    @GetMapping("/{id}")
    public AsyncTaskResponseDto getStatus(
            @PathVariable String id,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return asyncTaskService.getById(id, user.getUser());
    }

}
