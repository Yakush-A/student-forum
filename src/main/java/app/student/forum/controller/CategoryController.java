package app.student.forum.controller;

import app.student.forum.dto.category.CategoryRequestDto;
import app.student.forum.dto.category.CategoryResponseDto;
import app.student.forum.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
@Tag(name = "Categories", description = "Управление категориями")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(
            summary = "Создать категорию",
            description = "Создает новую категорию (доступно ADMIN и MODERATOR)"
    )
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public CategoryResponseDto create(
            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные категории",
                    required = true
            )
            @RequestBody CategoryRequestDto categoryRequestDto
    ) {
        return categoryService.create(categoryRequestDto);
    }

    @Operation(
            summary = "Получить список категорий",
            description = "Возвращает список категорий с пагинацией"
    )
    @ApiResponse(responseCode = "200", description = "Список категорий получен")
    @GetMapping
    public Page<CategoryResponseDto> getAll(
            @Parameter(hidden = true) Pageable pageable
    ) {
        return categoryService.getAll(pageable);
    }

    @Operation(
            summary = "Получить категорию по ID",
            description = "Возвращает категорию по её идентификатору"
    )
    @GetMapping("/{id}")
    public CategoryResponseDto getById(
            @Parameter(description = "ID категории", example = "1")
            @PathVariable Long id
    ) {
        return categoryService.getById(id);
    }

    @Operation(
            summary = "Обновить категорию",
            description = "Обновляет категорию (доступно только ADMIN)"
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponseDto update(

            @Parameter(description = "ID категории", example = "1")
            @PathVariable Long id,

            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Обновленные данные категории",
                    required = true
            )
            @RequestBody CategoryRequestDto categoryDto
    ) {
        return categoryService.update(id, categoryDto);
    }

    @Operation(
            summary = "Удалить категорию",
            description = "Удаляет категорию (доступно только ADMIN)"
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(

            @Parameter(description = "ID категории", example = "1")
            @PathVariable Long id
    ) {
        categoryService.delete(id);
    }
}
