package app.student.forum.controller;

import app.student.forum.model.dto.CommentRequestDto;
import app.student.forum.model.dto.CommentResponseDto;
import app.student.forum.service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public CommentResponseDto create(@RequestBody CommentRequestDto commentRequestDto) {
        return commentService.create(commentRequestDto);
    }

    @GetMapping("/post/{postId}")
    public List<CommentResponseDto> getByPost(@PathVariable Long postId) {
        return commentService.getByPost(postId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        commentService.delete(id);
    }

}
