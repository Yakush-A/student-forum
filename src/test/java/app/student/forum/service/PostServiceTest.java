package app.student.forum.service;

import app.student.forum.dto.post.PostDetailsResponseDto;
import app.student.forum.dto.post.PostRequestDto;
import app.student.forum.dto.post.PostResponseDto;
import app.student.forum.dto.post.PostUpdateDto;
import app.student.forum.entity.*;
import app.student.forum.exception.AccessDeniedException;
import app.student.forum.exception.NotFoundException;
import app.student.forum.mapper.PostMapper;
import app.student.forum.repository.CategoryRepository;
import app.student.forum.repository.PostRepository;
import app.student.forum.repository.TagRepository;
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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    private static final Long POST_ID = 1L;
    private static final Long CATEGORY_ID = 10L;
    private static final Long TAG_ID = 100L;
    private static final int PAGE_SIZE = 10;

    @Mock
    private PostRepository postRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private PostMapper postMapper;

    @InjectMocks
    private PostService postService;

    @Test
    void getAllPostsShouldUseCache() {
        Pageable pageable = PageRequest.of(0, PAGE_SIZE);

        Post post = new Post();
        Page<Post> page = new PageImpl<>(List.of(post));

        PostResponseDto dto = new PostResponseDto();

        when(postRepository.findAll(pageable)).thenReturn(page);
        when(postMapper.toDto(post)).thenReturn(dto);

        Page<PostResponseDto> first = postService.getAllPosts(pageable);
        Page<PostResponseDto> second = postService.getAllPosts(pageable);

        assertEquals(first, second);
        verify(postRepository, times(1)).findAll(pageable);
    }

    @Test
    void createShouldSavePost() {
        User user = new User();
        user.setId(1L);

        PostRequestDto dto = new PostRequestDto(CATEGORY_ID, "content", List.of(TAG_ID));

        Category category = new Category();
        category.setId(CATEGORY_ID);

        Tag tag = new Tag();
        tag.setId(TAG_ID);

        Post saved = new Post();
        saved.setId(POST_ID);

        PostResponseDto response = new PostResponseDto();

        when(categoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.of(category));
        when(tagRepository.findById(TAG_ID)).thenReturn(Optional.of(tag));
        when(postRepository.save(any(Post.class))).thenReturn(saved);
        when(postMapper.toDto(saved)).thenReturn(response);

        PostResponseDto result = postService.create(dto, user);

        assertEquals(response, result);

        verify(postRepository).save(any(Post.class));
    }

    @Test
    void createShouldThrowWhenCategoryNotFound() {
        User user = new User();
        user.setId(1L);

        PostRequestDto dto = new PostRequestDto(CATEGORY_ID, "content", List.of(TAG_ID));

        when(categoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> postService.create(dto, user));

        verifyNoMoreInteractions(tagRepository);
    }

    @Test
    void patchShouldUpdatePost() {
        User user = new User();
        user.setId(1L);

        Post post = new Post();
        post.setId(POST_ID);

        User author = new User();
        author.setId(1L);
        post.setAuthor(author);

        Category category = new Category();
        category.setId(CATEGORY_ID);

        Tag tag = new Tag();
        tag.setId(TAG_ID);

        PostUpdateDto dto = new PostUpdateDto("new", CATEGORY_ID, Set.of(TAG_ID));

        Post updated = new Post();
        updated.setId(POST_ID);

        PostResponseDto response = new PostResponseDto();

        when(postRepository.findById(POST_ID)).thenReturn(Optional.of(post));
        when(categoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.of(category));
        when(tagRepository.findById(TAG_ID)).thenReturn(Optional.of(tag));
        when(postRepository.save(post)).thenReturn(updated);
        when(postMapper.toDto(updated)).thenReturn(response);

        PostResponseDto result = postService.patch(POST_ID, dto, user);

        assertEquals(response, result);
        assertEquals("new", post.getContent());

        verify(postRepository).save(post);
    }

    @Test
    void patchShouldThrowAccessDenied() {
        User user = new User();
        user.setId(2L);

        Post post = new Post();
        post.setId(POST_ID);

        User author = new User();
        author.setId(1L);
        post.setAuthor(author);

        PostUpdateDto dto = new PostUpdateDto("new", CATEGORY_ID, Set.of(TAG_ID));

        when(postRepository.findById(POST_ID)).thenReturn(Optional.of(post));

        assertThrows(AccessDeniedException.class,
                () -> postService.patch(POST_ID, dto, user));

        verify(postRepository, never()).save(any());
    }

    @Test
    void deletePostByIdShouldDeleteWhenAuthor() {
        User user = new User();
        user.setId(1L);
        user.setRole(Role.USER);

        Post post = new Post();
        post.setId(POST_ID);

        User author = new User();
        author.setId(1L);
        post.setAuthor(author);

        when(postRepository.findById(POST_ID)).thenReturn(Optional.of(post));

        postService.deletePostById(POST_ID, user);

        verify(postRepository).delete(post);
    }

    @Test
    void deletePostByIdShouldDeleteWhenAdmin() {
        User user = new User();
        user.setId(2L);
        user.setRole(Role.ADMIN);

        Post post = new Post();
        post.setId(POST_ID);

        User author = new User();
        author.setId(1L);
        post.setAuthor(author);

        when(postRepository.findById(POST_ID)).thenReturn(Optional.of(post));

        postService.deletePostById(POST_ID, user);

        verify(postRepository).delete(post);
    }

    @Test
    void deletePostByIdShouldThrowAccessDenied() {
        User user = new User();
        user.setId(2L);
        user.setRole(Role.USER);

        Post post = new Post();
        post.setId(POST_ID);

        User author = new User();
        author.setId(1L);
        post.setAuthor(author);

        when(postRepository.findById(POST_ID)).thenReturn(Optional.of(post));

        assertThrows(AccessDeniedException.class,
                () -> postService.deletePostById(POST_ID, user));

        verify(postRepository, never()).delete(any());
    }

    @Test
    void getPostByIdShouldReturnPost() {
        Post post = new Post();
        post.setId(POST_ID);

        PostDetailsResponseDto response = new PostDetailsResponseDto();

        when(postRepository.findWithCommentsById(POST_ID)).thenReturn(Optional.of(post));
        when(postMapper.toDetailsDto(post)).thenReturn(response);

        PostDetailsResponseDto result = postService.getPostById(POST_ID);

        assertEquals(response, result);
    }

    @Test
    void getPostsByAuthorAndCategoryNameShouldUseCache() {
        Pageable pageable = PageRequest.of(0, PAGE_SIZE);

        Post post = new Post();
        Page<Post> page = new PageImpl<>(List.of(post));

        PostResponseDto dto = new PostResponseDto();

        when(postRepository.findAllWithFilters(any(), any(), eq(pageable))).thenReturn(page);
        when(postMapper.toDto(post)).thenReturn(dto);

        Page<PostResponseDto> first =
                postService.getPostsByAuthorAndCategoryName(1L, "cat", pageable, "false");
        Page<PostResponseDto> second =
                postService.getPostsByAuthorAndCategoryName(1L, "cat", pageable, "false");

        assertEquals(first, second);
        verify(postRepository, times(1))
                .findAllWithFilters(any(), any(), eq(pageable));
    }

    @Test
    void createShouldThrowWhenTagNotFound() {
        User user = new User();
        user.setId(1L);

        PostRequestDto dto = new PostRequestDto(CATEGORY_ID, "content", List.of(TAG_ID));

        Category category = new Category();
        category.setId(CATEGORY_ID);

        when(categoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.of(category));
        when(tagRepository.findById(TAG_ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> postService.create(dto, user));
    }

    @Test
    void patchShouldThrowNotFound() {
        User user = new User();
        user.setId(1L);

        PostUpdateDto dto = new PostUpdateDto("new", CATEGORY_ID, Set.of(TAG_ID));

        when(postRepository.findById(POST_ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> postService.patch(POST_ID, dto, user));
    }

    @Test
    void patchShouldThrowWhenCategoryNotFound() {
        User user = new User();
        user.setId(1L);

        Post post = new Post();
        post.setId(POST_ID);

        User author = new User();
        author.setId(1L);
        post.setAuthor(author);

        PostUpdateDto dto = new PostUpdateDto("new", CATEGORY_ID, Set.of(TAG_ID));

        when(postRepository.findById(POST_ID)).thenReturn(Optional.of(post));
        when(categoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> postService.patch(POST_ID, dto, user));
    }

    @Test
    void patchShouldThrowWhenTagNotFound() {
        User user = new User();
        user.setId(1L);

        Post post = new Post();
        post.setId(POST_ID);

        User author = new User();
        author.setId(1L);
        post.setAuthor(author);

        Category category = new Category();
        category.setId(CATEGORY_ID);

        PostUpdateDto dto = new PostUpdateDto("new", CATEGORY_ID, Set.of(TAG_ID));

        when(postRepository.findById(POST_ID)).thenReturn(Optional.of(post));
        when(categoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.of(category));
        when(tagRepository.findById(TAG_ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> postService.patch(POST_ID, dto, user));
    }

    @Test
    void deletePostByIdShouldDeleteWhenModerator() {
        User user = new User();
        user.setId(2L);
        user.setRole(Role.MODERATOR);

        Post post = new Post();
        post.setId(POST_ID);

        User author = new User();
        author.setId(1L);
        post.setAuthor(author);

        when(postRepository.findById(POST_ID)).thenReturn(Optional.of(post));

        postService.deletePostById(POST_ID, user);

        verify(postRepository).delete(post);
    }

    @Test
    void getPostByIdShouldThrowNotFound() {
        when(postRepository.findWithCommentsById(POST_ID))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> postService.getPostById(POST_ID));
    }

    @Test
    void getPostsByAuthorAndCategoryNameShouldReturnMappedPageWhenNativeTrue() {
        Pageable pageable = PageRequest.of(0, PAGE_SIZE);

        Long authorId = 1L;
        String category = "cat";

        Post post = new Post();
        post.setId(POST_ID);

        Page<Post> page = new PageImpl<>(List.of(post));

        PostResponseDto dto = new PostResponseDto();
        dto.setId(POST_ID);

        when(postRepository.findAllWithFiltersNative(authorId, category, pageable))
                .thenReturn(page);
        when(postMapper.toDto(post)).thenReturn(dto);

        Page<PostResponseDto> result =
                postService.getPostsByAuthorAndCategoryName(authorId, category, pageable, "true");

        assertEquals(1, result.getContent().size());
        assertEquals(dto, result.getContent().getFirst());

        verify(postRepository)
                .findAllWithFiltersNative(authorId, category, pageable);

        verify(postRepository, never())
                .findAllWithFilters(any(), any(), any());
    }

}
