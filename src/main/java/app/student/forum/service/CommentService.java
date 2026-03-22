package app.student.forum.service;

import app.student.forum.exception.AccessDeniedException;
import app.student.forum.exception.ErrorCode;
import app.student.forum.exception.NotFoundException;
import app.student.forum.mapper.CommentMapper;
import app.student.forum.dto.comment.CommentRequestDto;
import app.student.forum.dto.comment.CommentResponseDto;
import app.student.forum.entity.Comment;
import app.student.forum.entity.Post;
import app.student.forum.entity.Role;
import app.student.forum.entity.User;
import app.student.forum.repository.CommentRepository;
import app.student.forum.repository.PostRepository;
import app.student.forum.service.cache.CommentQueryKey;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Validated
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final PostRepository postRepository;
    private final Map<CommentQueryKey, Page<CommentResponseDto>> commentCache = new ConcurrentHashMap<>();

    public CommentResponseDto create(@Valid CommentRequestDto commentRequestDto, User user) {

        Post post = postRepository.findById(commentRequestDto.getPostId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));

        Comment comment = new Comment();

        comment.setContent(commentRequestDto.getContent());
        comment.setPost(post);
        comment.setAuthor(user);

        Comment saved = commentRepository.save(comment);

        commentCache.clear();
        return commentMapper.toDto(saved);
    }

    public CommentResponseDto update(Long id, @Valid CommentRequestDto commentRequestDto, User user) {

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getAuthor().getId().equals(user.getId())) {
            throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        }

        comment.setContent(commentRequestDto.getContent());

        comment.setUpdatedAt(LocalDateTime.now());

        Comment updated = commentRepository.save(comment);
        commentCache.clear();
        return commentMapper.toDto(updated);
    }

    public Page<CommentResponseDto> getByPost(Long postId, Pageable pageable) {

        return commentRepository.findByPostId(postId, pageable)
                .map(commentMapper::toDto);
    }

    public void delete(Long id, User user) {

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.COMMENT_NOT_FOUND));

        boolean isAuthor = user.getId().equals(comment.getAuthor().getId());
        boolean isAdmin = user.getRole().equals(Role.ADMIN);
        boolean isModerator = user.getRole().equals(Role.MODERATOR);

        if (isAuthor || isAdmin || isModerator) {
            commentRepository.delete(comment);
            commentCache.clear();
        } else {
            throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        }
    }

}
