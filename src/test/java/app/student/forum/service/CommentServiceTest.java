package app.student.forum.service;

import app.student.forum.dto.comment.CommentRequestDto;
import app.student.forum.dto.comment.CommentResponseDto;
import app.student.forum.entity.Comment;
import app.student.forum.entity.Post;
import app.student.forum.entity.Role;
import app.student.forum.entity.User;
import app.student.forum.exception.AccessDeniedException;
import app.student.forum.exception.NotFoundException;
import app.student.forum.mapper.CommentMapper;
import app.student.forum.repository.CommentRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    private static final Long COMMENT_ID = 1L;
    private static final Long POST_ID = 10L;
    private static final int PAGE_SIZE = 10;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private CommentService commentService;

    @Test
    void createShouldSaveComment() {
        User user = new User();
        user.setId(1L);

        CommentRequestDto dto = new CommentRequestDto("text", POST_ID);

        Post post = new Post();
        post.setId(POST_ID);

        Comment saved = new Comment();
        saved.setId(COMMENT_ID);

        CommentResponseDto response = new CommentResponseDto();

        when(postRepository.findById(POST_ID)).thenReturn(Optional.of(post));
        when(commentRepository.save(any(Comment.class))).thenReturn(saved);
        when(commentMapper.toDto(saved)).thenReturn(response);

        CommentResponseDto result = commentService.create(dto, user);

        assertEquals(response, result);

        verify(postRepository).findById(POST_ID);
        verify(commentRepository).save(any(Comment.class));
        verify(commentMapper).toDto(saved);
    }

    @Test
    void createShouldThrowNotFoundWhenPostMissing() {
        User user = new User();
        user.setId(1L);

        CommentRequestDto dto = new CommentRequestDto("text", POST_ID);

        when(postRepository.findById(POST_ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> commentService.create(dto, user));

        verify(postRepository).findById(POST_ID);
        verifyNoMoreInteractions(commentRepository);
    }

    @Test
    void updateShouldUpdateWhenAuthor() {
        User user = new User();
        user.setId(1L);

        Comment comment = new Comment();
        comment.setId(COMMENT_ID);

        User author = new User();
        author.setId(1L);
        comment.setAuthor(author);

        CommentRequestDto dto = new CommentRequestDto("new text", POST_ID);

        Comment updated = new Comment();
        updated.setId(COMMENT_ID);

        CommentResponseDto response = new CommentResponseDto();

        when(commentRepository.findById(COMMENT_ID)).thenReturn(Optional.of(comment));
        when(commentRepository.save(comment)).thenReturn(updated);
        when(commentMapper.toDto(updated)).thenReturn(response);

        CommentResponseDto result = commentService.update(COMMENT_ID, dto, user);

        assertEquals(response, result);
        assertEquals("new text", comment.getContent());

        verify(commentRepository).save(comment);
    }

    @Test
    void updateShouldThrowAccessDeniedWhenNotAuthor() {
        User user = new User();
        user.setId(2L);

        Comment comment = new Comment();
        comment.setId(COMMENT_ID);

        User author = new User();
        author.setId(1L);
        comment.setAuthor(author);

        CommentRequestDto dto = new CommentRequestDto("text", POST_ID);

        when(commentRepository.findById(COMMENT_ID)).thenReturn(Optional.of(comment));

        assertThrows(AccessDeniedException.class,
                () -> commentService.update(COMMENT_ID, dto, user));

        verify(commentRepository, never()).save(any());
    }

    @Test
    void updateShouldThrowNotFound() {
        User user = new User();
        user.setId(1L);

        CommentRequestDto dto = new CommentRequestDto("text", POST_ID);

        when(commentRepository.findById(COMMENT_ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> commentService.update(COMMENT_ID, dto, user));
    }

    @Test
    void getByPostShouldReturnPage() {
        Pageable pageable = PageRequest.of(0, PAGE_SIZE);

        Comment comment = new Comment();
        Page<Comment> page = new PageImpl<>(List.of(comment));

        CommentResponseDto dto = new CommentResponseDto();

        when(commentRepository.findByPostId(POST_ID, pageable)).thenReturn(page);
        when(commentMapper.toDto(comment)).thenReturn(dto);

        Page<CommentResponseDto> result =
                commentService.getByPost(POST_ID, pageable);

        assertEquals(1, result.getTotalElements());

        verify(commentRepository).findByPostId(POST_ID, pageable);
        verify(commentMapper).toDto(comment);
    }

    @Test
    void deleteShouldDeleteWhenAuthor() {
        User user = new User();
        user.setId(1L);
        user.setRole(Role.USER);

        Comment comment = new Comment();
        comment.setId(COMMENT_ID);

        User author = new User();
        author.setId(1L);
        comment.setAuthor(author);

        when(commentRepository.findById(COMMENT_ID)).thenReturn(Optional.of(comment));

        commentService.delete(COMMENT_ID, user);

        verify(commentRepository).delete(comment);
    }

    @Test
    void deleteShouldDeleteWhenAdmin() {
        User user = new User();
        user.setId(2L);
        user.setRole(Role.ADMIN);

        Comment comment = new Comment();
        comment.setId(COMMENT_ID);

        User author = new User();
        author.setId(1L);
        comment.setAuthor(author);

        when(commentRepository.findById(COMMENT_ID)).thenReturn(Optional.of(comment));

        commentService.delete(COMMENT_ID, user);

        verify(commentRepository).delete(comment);
    }

    @Test
    void deleteShouldDeleteWhenModerator() {
        User user = new User();
        user.setId(2L);
        user.setRole(Role.MODERATOR);

        Comment comment = new Comment();
        comment.setId(COMMENT_ID);

        User author = new User();
        author.setId(1L);
        comment.setAuthor(author);

        when(commentRepository.findById(COMMENT_ID)).thenReturn(Optional.of(comment));

        commentService.delete(COMMENT_ID, user);

        verify(commentRepository).delete(comment);
    }

    @Test
    void deleteShouldThrowAccessDenied() {
        User user = new User();
        user.setId(2L);
        user.setRole(Role.USER);

        Comment comment = new Comment();
        comment.setId(COMMENT_ID);

        User author = new User();
        author.setId(1L);
        comment.setAuthor(author);

        when(commentRepository.findById(COMMENT_ID)).thenReturn(Optional.of(comment));

        assertThrows(AccessDeniedException.class,
                () -> commentService.delete(COMMENT_ID, user));

        verify(commentRepository, never()).delete(any());
    }

    @Test
    void deleteShouldThrowNotFound() {
        User user = new User();
        user.setId(1L);
        user.setRole(Role.USER);

        when(commentRepository.findById(COMMENT_ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> commentService.delete(COMMENT_ID, user));
    }
}
