package app.student.forum.service;

import app.student.forum.exception.BadRequestException;
import app.student.forum.exception.NotFoundException;
import app.student.forum.mapper.CategoryMapper;
import app.student.forum.model.dto.category.CategoryRequestDto;
import app.student.forum.model.dto.category.CategoryResponseDto;
import app.student.forum.model.entity.Category;
import app.student.forum.model.entity.Post;
import app.student.forum.repository.CategoryRepository;
import app.student.forum.repository.PostRepository;
import app.student.forum.service.cache.CategoryQueryKey;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class CategoryService {

    public static final String CATEGORY_NAME_EXIST = "Category name already exist";
    public static final String CATEGORY_NOT_FOUND = "Category not found";
    public static final String CATEGORY_NAME_IS_EMPTY = "Category name is empty";

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final PostRepository postRepository;
    private final Map<CategoryQueryKey, Page<CategoryResponseDto>> categoryCache = new ConcurrentHashMap<>();

    public CategoryResponseDto create(CategoryRequestDto categoryRequestDto) {

        if (categoryRequestDto.getName() == null || categoryRequestDto.getName().isEmpty()) {
            throw new BadRequestException(CATEGORY_NAME_IS_EMPTY);
        } else if (categoryRepository.existsByNameIgnoreCase(categoryRequestDto.getName())) {
            throw new BadRequestException(CATEGORY_NAME_EXIST);
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
                .orElseThrow(() -> new NotFoundException(CATEGORY_NOT_FOUND));

        return categoryMapper.toDto(category);
    }

    public CategoryResponseDto update(Long id, CategoryRequestDto categoryDto) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(CATEGORY_NOT_FOUND));

        if (categoryDto.getName() == null || categoryDto.getName().isEmpty()) {
            throw new BadRequestException(CATEGORY_NAME_IS_EMPTY);
        }

        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());

        Category saved = categoryRepository.save(category);
        categoryCache.clear();

        return categoryMapper.toDto(saved);
    }

    public void delete(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(CATEGORY_NOT_FOUND));

        List<Post> posts = postRepository.findByCategoryId(id);

        posts.forEach(post -> post.setCategory(null));

        categoryCache.clear();
        categoryRepository.delete(category);
    }
}
