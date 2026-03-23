package app.student.forum.controller;

import app.student.forum.dto.comment.CommentRequestDto;
import app.student.forum.dto.comment.CommentResponseDto;
import app.student.forum.security.CustomUserDetails;
import app.student.forum.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@Tag(name = "Comments", description = "Работа с комментариями")
public class CommentController {

    private final CommentService commentService;

    @Operation(
            summary = "Создать комментарий",
            description = "Создает комментарий для поста от имени авторизованного пользователя"
    )
    @PostMapping
    public CommentResponseDto create(
            @Valid
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для создания комментария",
                    required = true
            )
            CommentRequestDto commentRequestDto,

            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return commentService.create(commentRequestDto, customUserDetails.getUser());
    }

    @Operation(
            summary = "Получить комментарии по посту",
            description = "Возвращает страницу комментариев для указанного поста"
    )
    @GetMapping("/post/{postId}")
    public Page<CommentResponseDto> getByPostId(

            @Parameter(description = "ID поста", example = "42")
            @PathVariable Long postId,

            @Parameter(hidden = true)
            Pageable pageable
    ) {
        return commentService.getByPost(postId, pageable);
    }

    @Operation(
            summary = "Удалить комментарий",
            description = "Удаляет комментарий, если пользователь является автором или администратором"
    )
    @DeleteMapping("/{id}")
    public void delete(

            @Parameter(description = "ID комментария", example = "1")
            @PathVariable Long id,

            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        commentService.delete(id, customUserDetails.getUser());
    }

    @Operation(
            summary = "Обновить комментарий",
            description = "Обновляет текст комментария"
    )
    @PatchMapping("/{id}")
    public CommentResponseDto update(

            @Parameter(description = "ID комментария", example = "1")
            @PathVariable Long id,

            @Valid
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Новые данные комментария",
                    required = true
            )
            CommentRequestDto commentRequestDto,

            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return commentService.update(id, commentRequestDto, customUserDetails.getUser());
    }
}
