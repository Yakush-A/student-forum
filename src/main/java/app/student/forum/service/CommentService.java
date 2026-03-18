package app.student.forum.service;

import app.student.forum.exception.BadRequestException;
import app.student.forum.exception.ForbiddenAccessException;
import app.student.forum.exception.NotFoundException;
import app.student.forum.exception.PostNotFoundException;
import app.student.forum.mapper.CommentMapper;
import app.student.forum.model.dto.comment.CommentRequestDto;
import app.student.forum.model.dto.comment.CommentResponseDto;
import app.student.forum.model.entity.Comment;
import app.student.forum.model.entity.Post;
import app.student.forum.model.entity.Role;
import app.student.forum.model.entity.User;
import app.student.forum.repository.CommentRepository;
import app.student.forum.repository.PostRepository;
import app.student.forum.service.cache.CommentQueryKey;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final PostRepository postRepository;
    private final Map<CommentQueryKey, Page<CommentResponseDto>> commentCache = new ConcurrentHashMap<>();

    public CommentResponseDto create(CommentRequestDto commentRequestDto, User user) {

        Post post = postRepository.findById(commentRequestDto.getPostId())
                .orElseThrow(() -> new NotFoundException("Post not found"));

        Comment comment = new Comment();

        comment.setContent(commentRequestDto.getContent());
        comment.setPost(post);
        comment.setAuthor(user);

        Comment saved = commentRepository.save(comment);

        commentCache.clear();
        return commentMapper.toDto(saved);
    }

    public CommentResponseDto update(Long id, CommentRequestDto commentRequestDto, User user) {

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comment not found"));

        if (!comment.getAuthor().getId().equals(user.getId())) {
            throw new ForbiddenAccessException("You are not allowed to update this comment");
        }

        if (comment.getContent().equals(commentRequestDto.getContent())) {
            throw new BadRequestException("Comment content must not be empty");
        } else {
            comment.setContent(commentRequestDto.getContent());
        }

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
                .orElseThrow(() -> new NotFoundException("Comment not found"));

        boolean isAuthor = user.getId().equals(comment.getAuthor().getId());
        boolean isAdmin = user.getRole().equals(Role.ADMIN);
        boolean isModerator = user.getRole().equals(Role.MODERATOR);

        if (isAuthor || isAdmin || isModerator) {
            commentRepository.delete(comment);
            commentCache.clear();
        } else {
            throw new ForbiddenAccessException("You are not allowed to delete this comment");
        }
    }

}
