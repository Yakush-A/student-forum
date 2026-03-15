package app.student.forum.controller;

import app.student.forum.model.dto.post.PostDetailsResponseDto;
import app.student.forum.model.dto.post.PostRequestDto;
import app.student.forum.model.dto.post.PostResponseDto;
import app.student.forum.model.dto.post.PostUpdateDto;
import app.student.forum.security.CustomUserDetails;
import app.student.forum.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public PostResponseDto createPost(
            @RequestBody PostRequestDto postRequestDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return postService.create(postRequestDto, customUserDetails.getUser());
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
    public PostResponseDto patchPost(
            @PathVariable Long id,
            @RequestBody CustomUserDetails customUserDetails,
            @RequestBody PostUpdateDto postUpdateDto
    ) {
        return postService.patch(id, postUpdateDto, customUserDetails.getUser());
    }

    @DeleteMapping("/{id}")
    public void deletePost(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        postService.deletePostById(id, customUserDetails.getUser());
    }

    @PatchMapping("/categorize/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<PostResponseDto> categorizePosts(
            @PathVariable Long categoryId
    ) {
        return postService.assignUncategorizedPostsToCategory(categoryId);
    }
}
