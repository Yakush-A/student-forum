package app.student.forum.service;

import app.student.forum.dto.category.CategoryRequestDto;
import app.student.forum.dto.category.CategoryResponseDto;
import app.student.forum.entity.Category;
import app.student.forum.entity.Post;
import app.student.forum.exception.ConflictException;
import app.student.forum.exception.NotFoundException;
import app.student.forum.mapper.CategoryMapper;
import app.student.forum.repository.CategoryRepository;
import app.student.forum.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    private static final Long CATEGORY_ID = 1L;
    private static final int PAGE_SIZE = 10;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void createShouldSaveCategory() {
        CategoryRequestDto dto = new CategoryRequestDto("java", "desc");

        Category category = new Category();
        category.setName("java");

        Category saved = new Category();
        saved.setId(CATEGORY_ID);
        saved.setName("java");

        CategoryResponseDto response = new CategoryResponseDto(CATEGORY_ID, "java", "desc", null);

        when(categoryRepository.existsByNameIgnoreCase("java")).thenReturn(false);
        when(categoryMapper.toEntity(dto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(saved);
        when(categoryMapper.toDto(saved)).thenReturn(response);

        CategoryResponseDto result = categoryService.create(dto);

        assertEquals(response, result);

        verify(categoryRepository).existsByNameIgnoreCase("java");
        verify(categoryMapper).toEntity(dto);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toDto(saved);
    }

    @Test
    void createShouldThrowConflictWhenExists() {
        CategoryRequestDto dto = new CategoryRequestDto("java", "desc");

        when(categoryRepository.existsByNameIgnoreCase("java")).thenReturn(true);

        assertThrows(ConflictException.class, () -> categoryService.create(dto));

        verify(categoryRepository).existsByNameIgnoreCase("java");
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    void getAllShouldUseCache() {
        Pageable pageable = PageRequest.of(0, PAGE_SIZE);

        Category category = new Category();
        Page<Category> page = new PageImpl<>(List.of(category));

        CategoryResponseDto dto = new CategoryResponseDto(1L, "java", "desc", null);

        when(categoryRepository.findAll(pageable)).thenReturn(page);
        when(categoryMapper.toDto(category)).thenReturn(dto);

        Page<CategoryResponseDto> first = categoryService.getAll(pageable);
        Page<CategoryResponseDto> second = categoryService.getAll(pageable);

        assertEquals(first, second);
        verify(categoryRepository, times(1)).findAll(pageable);
    }

    @Test
    void getByIdShouldReturnCategory() {
        Category category = new Category();
        category.setId(CATEGORY_ID);

        CategoryResponseDto response = new CategoryResponseDto(CATEGORY_ID, "java", "desc", null);

        when(categoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(response);

        CategoryResponseDto result = categoryService.getById(CATEGORY_ID);

        assertEquals(response, result);

        verify(categoryRepository).findById(CATEGORY_ID);
        verify(categoryMapper).toDto(category);
    }

    @Test
    void getByIdShouldThrowNotFound() {
        when(categoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> categoryService.getById(CATEGORY_ID));

        verify(categoryRepository).findById(CATEGORY_ID);
    }

    @Test
    void updateShouldUpdateCategory() {
        Category category = new Category();
        category.setId(CATEGORY_ID);
        category.setName("old");
        category.setDescription("old");

        CategoryRequestDto dto = new CategoryRequestDto("new", "newDesc");

        Category saved = new Category();
        saved.setId(CATEGORY_ID);
        saved.setName("new");
        saved.setDescription("newDesc");

        CategoryResponseDto response =
                new CategoryResponseDto(CATEGORY_ID, "new", "newDesc", null);

        when(categoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.of(category));
        when(categoryRepository.existsByNameIgnoreCase("new")).thenReturn(false);
        when(categoryRepository.save(category)).thenReturn(saved);
        when(categoryMapper.toDto(saved)).thenReturn(response);

        CategoryResponseDto result = categoryService.update(CATEGORY_ID, dto);

        assertEquals(response, result);
        assertEquals("new", category.getName());
        assertEquals("newDesc", category.getDescription());

        verify(categoryRepository).save(category);
    }

    @Test
    void updateShouldThrowConflictWhenNameExists() {
        Category category = new Category();
        category.setId(CATEGORY_ID);

        CategoryRequestDto dto = new CategoryRequestDto("java", "desc");

        when(categoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.of(category));
        when(categoryRepository.existsByNameIgnoreCase("java")).thenReturn(true);

        assertThrows(ConflictException.class,
                () -> categoryService.update(CATEGORY_ID, dto));
    }

    @Test
    void updateShouldThrowNotFound() {
        CategoryRequestDto dto = new CategoryRequestDto("java", "desc");

        when(categoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> categoryService.update(CATEGORY_ID, dto));
    }

    @Test
    void deleteShouldDeleteCategoryAndClearPosts() {
        Category category = new Category();
        category.setId(CATEGORY_ID);

        Post post1 = new Post();
        post1.setCategory(category);

        Post post2 = new Post();
        post2.setCategory(category);

        List<Post> posts = List.of(post1, post2);

        when(categoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.of(category));
        when(postRepository.findByCategoryId(CATEGORY_ID)).thenReturn(posts);

        categoryService.delete(CATEGORY_ID);

        assertNull(post1.getCategory());
        assertNull(post2.getCategory());

        verify(postRepository).findByCategoryId(CATEGORY_ID);
        verify(categoryRepository).delete(category);
    }

    @Test
    void deleteShouldThrowNotFound() {
        when(categoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> categoryService.delete(CATEGORY_ID));

        verify(categoryRepository).findById(CATEGORY_ID);
    }
}
