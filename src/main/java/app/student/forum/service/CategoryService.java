package app.student.forum.service;

import app.student.forum.mapper.CategoryMapper;
import app.student.forum.model.dto.category.CategoryRequestDto;
import app.student.forum.model.dto.category.CategoryResponseDto;
import app.student.forum.model.entity.Category;
import app.student.forum.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryResponseDto create(CategoryRequestDto categoryRequestDto) {

        Category category = categoryMapper.toEntity(categoryRequestDto);
        Category savedCategory = categoryRepository.save(category);

        return categoryMapper.toDto(savedCategory);
    }

    public List<CategoryResponseDto> getAll() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    public CategoryResponseDto getById(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        return categoryMapper.toDto(category);
    }

    public CategoryResponseDto update(Long id, CategoryRequestDto categoryDto) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());

        Category saved = categoryRepository.save(category);

        return categoryMapper.toDto(saved);
    }

    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
