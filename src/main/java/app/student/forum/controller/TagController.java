package app.student.forum.controller;

import app.student.forum.dto.tag.TagRequestDto;
import app.student.forum.dto.tag.TagResponseDto;
import app.student.forum.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
@Tag(name = "Tags", description = "Управление тегами")
public class TagController {

    private final TagService tagService;

    @Operation(
            summary = "Создать тег",
            description = "Создает новый тег (доступно ADMIN и MODERATOR)"
    )
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public TagResponseDto create(
            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для создания тега",
                    required = true
            )
            @RequestBody TagRequestDto tagRequestDto
    ) {
        return tagService.create(tagRequestDto);
    }

    @Operation(
            summary = "Получить список тегов",
            description = "Возвращает список тегов с пагинацией"
    )
    @GetMapping
    public Page<TagResponseDto> getTags(
            @Parameter(hidden = true) Pageable pageable
    ) {
        return tagService.getAll(pageable);
    }

    @Operation(
            summary = "Получить тег по ID",
            description = "Возвращает тег по его идентификатору"
    )
    @GetMapping("/{id}")
    public TagResponseDto getById(

            @Parameter(description = "ID тега", example = "1")
            @PathVariable Long id
    ) {
        return tagService.getById(id);
    }

    @Operation(
            summary = "Обновить тег",
            description = "Обновляет тег (доступно ADMIN и MODERATOR)"
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public TagResponseDto update(

            @Parameter(description = "ID тега", example = "1")
            @PathVariable Long id,

            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Новые данные тега",
                    required = true
            )
            @RequestBody TagRequestDto tagRequestDto
    ) {
        return tagService.update(id, tagRequestDto);
    }

    @Operation(
            summary = "Удалить тег",
            description = "Удаляет тег (доступно ADMIN и MODERATOR)"
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public void delete(

            @Parameter(description = "ID тега", example = "1")
            @PathVariable Long id
    ) {
        tagService.delete(id);
    }

    @Operation(
            summary = "Поиск тегов",
            description = "Ищет теги по имени (частичное совпадение)"
    )
    @GetMapping("/search")
    public Page<TagResponseDto> search(

            @Parameter(description = "Название тега", example = "java")
            @RequestParam String name,

            @Parameter(hidden = true)
            Pageable pageable
    ) {
        return tagService.findAllByName(pageable, name);
    }
}
