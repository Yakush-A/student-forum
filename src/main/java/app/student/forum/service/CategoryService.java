package app.student.forum.service;

import app.student.forum.mapper.CategoryMapper;
import app.student.forum.model.dto.CategoryDto;
import app.student.forum.model.entity.Category;
import app.student.forum.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public CategoryDto create(CategoryDto categoryDto) {

        Category category = categoryMapper.toEntity(categoryDto);
        Category savedCategory = categoryRepository.save(category);

        return categoryMapper.toDto(savedCategory);
    }

    public List<CategoryDto> getAll() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    public CategoryDto getById(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        return categoryMapper.toDto(category);
    }

    public CategoryDto update(Long id, CategoryDto categoryDto) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());

        Category saved = categoryRepository.save(category);

        return categoryMapper.toDto(saved);
    }

    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
