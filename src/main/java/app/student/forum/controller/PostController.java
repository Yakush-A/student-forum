package app.student.forum.controller;

import app.student.forum.model.dto.PostDetailsResponseDto;
import app.student.forum.model.dto.PostRequestDto;
import app.student.forum.model.dto.PostResponseDto;
import app.student.forum.model.dto.PostUpdateDto;
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

    @PostMapping
    public PostResponseDto createPost(@RequestBody PostRequestDto postRequestDto) {
        return postService.create(postRequestDto);
    }

    @GetMapping
    public List<PostResponseDto> getAllPosts(@RequestParam(required = false) Long id) {
        if (id != null) {
            return postService.getPostsByAuthor(id);
        }
        return postService.getAllPosts();
    }

    @GetMapping("/{id}")
    public PostDetailsResponseDto getPost(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    @PatchMapping("/{id}")
    public PostResponseDto patchPost(@PathVariable Long id, @RequestBody PostUpdateDto postUpdateDto) {
        return postService.patch(id, postUpdateDto);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id) {
        postService.deletePostById(id);
    }

    @PatchMapping("/rename/{id}")
    public PostDetailsResponseDto renamePost(@PathVariable Long id, @RequestParam(required = true) String doTransactional) {
        if (doTransactional.equals("true")) {
            postService.renameWithTransaction(id);
        } else if (doTransactional.equals("false")) {
            postService.renameWithoutTransaction(id);
        }
        return postService.getPostById(id);
    }
}
