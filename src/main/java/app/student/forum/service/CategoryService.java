package app.student.forum.service;

import app.student.forum.exception.ConflictException;
import app.student.forum.exception.ErrorCode;
import app.student.forum.exception.NotFoundException;
import app.student.forum.mapper.CategoryMapper;
import app.student.forum.dto.category.CategoryRequestDto;
import app.student.forum.dto.category.CategoryResponseDto;
import app.student.forum.entity.Category;
import app.student.forum.entity.Post;
import app.student.forum.repository.CategoryRepository;
import app.student.forum.repository.PostRepository;
import app.student.forum.service.cache.CategoryQueryKey;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Validated
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final PostRepository postRepository;
    private final Map<CategoryQueryKey, Page<CategoryResponseDto>> categoryCache = new ConcurrentHashMap<>();

    public CategoryResponseDto create(@Valid CategoryRequestDto categoryRequestDto) {

        if (categoryRepository.existsByNameIgnoreCase(categoryRequestDto.getName())) {
            throw new ConflictException(ErrorCode.CATEGORY_ALREADY_EXISTS);
        }

        Category category = categoryMapper.toEntity(categoryRequestDto);
        Category savedCategory = categoryRepository.save(category);
        categoryCache.clear();

        return categoryMapper.toDto(savedCategory);
    }

    public Page<CategoryResponseDto> getAll(Pageable pageable) {

        CategoryQueryKey categoryQueryKey = new CategoryQueryKey(null, pageable);
        return categoryCache.computeIfAbsent(
                categoryQueryKey,
                k -> categoryRepository.findAll(pageable).map(categoryMapper::toDto)
        );

    }

    public CategoryResponseDto getById(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CATEGORY_NOT_FOUND));

        return categoryMapper.toDto(category);
    }

    public CategoryResponseDto update(Long id, @Valid CategoryRequestDto categoryDto) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        if (categoryRepository.existsByNameIgnoreCase(categoryDto.getName())) {
            throw new ConflictException(ErrorCode.CATEGORY_ALREADY_EXISTS);
        }
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());

        Category saved = categoryRepository.save(category);
        categoryCache.clear();

        return categoryMapper.toDto(saved);
    }

    public void delete(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CATEGORY_NOT_FOUND));

        List<Post> posts = postRepository.findByCategoryId(id);

        posts.forEach(post -> post.setCategory(null));

        categoryCache.clear();
        categoryRepository.delete(category);
    }
}
