package app.student.forum.controller;

import app.student.forum.model.dto.comment.CommentRequestDto;
import app.student.forum.model.dto.comment.CommentResponseDto;
import app.student.forum.security.CustomUserDetails;
import app.student.forum.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public CommentResponseDto create(
            @RequestBody CommentRequestDto commentRequestDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return commentService.create(commentRequestDto, customUserDetails.getUser());
    }

    @GetMapping("/post/{postId}")
    public List<CommentResponseDto> getByPost(@PathVariable Long postId) {
        return commentService.getByPost(postId);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        commentService.delete(id, customUserDetails.getUser());
    }

    @PatchMapping("/{id}")
    public CommentResponseDto update(
            @PathVariable Long id,
            @RequestBody CommentRequestDto commentRequestDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return commentService.update(id, commentRequestDto, customUserDetails.getUser());
    }
}
