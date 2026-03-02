package app.student.forum.controller;

import app.student.forum.model.dto.PostDto;
import app.student.forum.service.PostService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public List<PostDto> getAllPosts(@RequestParam(required = false) String author) {
        if (author != null) {
            return postService.getPostsByAuthor(author);
        }
        return postService.getAllPosts();
    }

    @GetMapping("/{id}")
    public PostDto getPost(@PathVariable Long id) {
        return postService.getPostById(id);
    }
}
