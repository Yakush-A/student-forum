package app.student.forum.controller;

import app.student.forum.dto.post.PostDetailsResponseDto;
import app.student.forum.dto.post.PostRequestDto;
import app.student.forum.dto.post.PostResponseDto;
import app.student.forum.dto.post.PostUpdateDto;
import app.student.forum.security.CustomUserDetails;
import app.student.forum.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@Tag(name = "Posts", description = "Работа с постами")
public class PostController {

    private final PostService postService;

    @Operation(
            summary = "Создать пост",
            description = "Создает новый пост от имени авторизованного пользователя"
    )
    @PostMapping
    public PostResponseDto createPost(
            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для создания поста",
                    required = true
            )
            @RequestBody PostRequestDto postRequestDto,

            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return postService.create(postRequestDto, customUserDetails.getUser());
    }

    @Operation(
            summary = "Получить список постов",
            description = "Возвращает список постов с возможностью фильтрации и пагинации"
    )
    @GetMapping
    public Page<PostResponseDto> getAllPosts(

            @Parameter(description = "Фильтр по ID автора", example = "1")
            @RequestParam(required = false) Long authorId,

            @Parameter(description = "Фильтр по названию категории", example = "Technology")
            @RequestParam(required = false) String categoryName,

            @Parameter(description = "Использовать native query", example = "false")
            @RequestParam(required = false) String doNative,

            @Parameter(hidden = true)
            Pageable pageable
    ) {
        if (authorId != null || categoryName != null) {
            if (doNative == null) {
                doNative = "false";
            }
            return postService.getPostsByAuthorAndCategoryName(authorId, categoryName, pageable, doNative);
        }
        return postService.getAllPosts(pageable);
    }

    @Operation(
            summary = "Получить пост по ID",
            description = "Возвращает детальную информацию о посте"
    )
    @GetMapping("/{id}")
    public PostDetailsResponseDto getPost(

            @Parameter(description = "ID поста", example = "1")
            @PathVariable Long id
    ) {
        return postService.getPostById(id);
    }

    @Operation(
            summary = "Частично обновить пост",
            description = "Обновляет пост (доступно автору поста)"
    )
    @PatchMapping("/{id}")
    public PostResponseDto patchPost(

            @Parameter(description = "ID поста", example = "1")
            @PathVariable Long id,

            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails customUserDetails,

            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для обновления поста",
                    required = true
            )
            @RequestBody PostUpdateDto postUpdateDto
    ) {
        return postService.patch(id, postUpdateDto, customUserDetails.getUser());
    }

    @Operation(
            summary = "Удалить пост",
            description = "Удаляет пост (доступно автору или администратору)"
    )
    @DeleteMapping("/{id}")
    public void deletePost(

            @Parameter(description = "ID поста", example = "1")
            @PathVariable Long id,

            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        postService.deletePostById(id, customUserDetails.getUser());
    }

}
