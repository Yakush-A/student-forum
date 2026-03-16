package app.student.forum.service;

import app.student.forum.exception.CategoryNotFoundException;
import app.student.forum.mapper.CategoryMapper;
import app.student.forum.model.dto.category.CategoryRequestDto;
import app.student.forum.model.dto.category.CategoryResponseDto;
import app.student.forum.model.entity.Category;
import app.student.forum.model.entity.Post;
import app.student.forum.repository.CategoryRepository;
import app.student.forum.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final PostRepository postRepository;

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
                .orElseThrow(CategoryNotFoundException::new);

        return categoryMapper.toDto(category);
    }

    public CategoryResponseDto update(Long id, CategoryRequestDto categoryDto) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(CategoryNotFoundException::new);

        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());

        Category saved = categoryRepository.save(category);

        return categoryMapper.toDto(saved);
    }

    public void delete(Long id) {

        Category category = categoryRepository.findById(id)
                        .orElseThrow(CategoryNotFoundException::new);

        List<Post> posts = postRepository.findByCategoryId(id);

        posts.forEach(post -> post.setCategory(null));

        categoryRepository.delete(category);
    }
}
