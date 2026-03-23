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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Validated
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final PostRepository postRepository;
    private final Map<CommentQueryKey, Page<CommentResponseDto>> commentCache = new ConcurrentHashMap<>();

    public CommentResponseDto create(@Valid CommentRequestDto commentRequestDto, User user) {
        log.info("User {} creating comment for post {}", user.getId(), commentRequestDto.getPostId());

        Post post = postRepository.findById(commentRequestDto.getPostId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));

        Comment comment = new Comment();

        comment.setContent(commentRequestDto.getContent());
        comment.setPost(post);
        comment.setAuthor(user);

        Comment saved = commentRepository.save(comment);

        commentCache.clear();
        log.debug("Comment cache cleared after create");

        log.info("Comment created {} by user {}", saved.getId(), user.getId());
        return commentMapper.toDto(saved);
    }

    public CommentResponseDto update(Long id, @Valid CommentRequestDto commentRequestDto, User user) {
        log.info("User {} updating comment {}", user.getId(), id);

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getAuthor().getId().equals(user.getId())) {
            log.warn("Access denied! User {} is not author of comment {}", user.getId(), id);
            throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        }

        comment.setContent(commentRequestDto.getContent());

        comment.setUpdatedAt(LocalDateTime.now());

        Comment updated = commentRepository.save(comment);
        commentCache.clear();
        log.debug("Comment cache cleared after update");

        log.info("Comment updated {} by user {}", updated.getId(), user.getId());
        return commentMapper.toDto(updated);
    }

    public Page<CommentResponseDto> getByPost(Long postId, Pageable pageable) {
        log.debug("Getting comments for post {}", postId);

        return commentRepository.findByPostId(postId, pageable)
                .map(commentMapper::toDto);
    }

    public void delete(Long id, User user) {
        log.info("User {} deleting comment {}", user.getId(), id);

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.COMMENT_NOT_FOUND));

        boolean isAuthor = user.getId().equals(comment.getAuthor().getId());
        boolean isAdmin = user.getRole().equals(Role.ADMIN);
        boolean isModerator = user.getRole().equals(Role.MODERATOR);

        if (isAuthor || isAdmin || isModerator) {
            commentRepository.delete(comment);
            commentCache.clear();
            log.debug("Comment cache cleared after delete");
            log.info("Comment deleted {} by user {}", comment.getId(), user.getId());
        } else {
            log.warn("Access denied! User {} can not delete comment {}", user.getId(), id);
            throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        }
    }

}
