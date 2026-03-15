package app.student.forum.service;

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
import app.student.forum.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentResponseDto create(CommentRequestDto commentRequestDto, User user) {

        Post post = postRepository.findById(commentRequestDto.getPostId())
                .orElseThrow(PostNotFoundException::new);

        Comment comment = new Comment();

        comment.setContent(commentRequestDto.getContent());
        comment.setPost(post);
        comment.setAuthor(user);

        Comment saved = commentRepository.save(comment);

        return commentMapper.toDto(saved);
    }

    public CommentResponseDto update(Long id, CommentRequestDto commentRequestDto, User user) {

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        if (!comment.getAuthor().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to update this comment");
        }

        if (comment.getContent().equals(commentRequestDto.getContent())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Comment content must not be empty");
        } else {
            comment.setContent(commentRequestDto.getContent());
        }

        comment.setUpdatedAt(LocalDateTime.now());

        Comment updated = commentRepository.save(comment);
        return commentMapper.toDto(updated);
    }

    public List<CommentResponseDto> getByPost(Long postId) {

        return commentRepository.findByPostId(postId)
                .stream()
                .map(commentMapper::toDto)
                .toList();
    }

    public void delete(Long id, User user) {

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        boolean isAuthor = user.getId().equals(id);
        boolean isAdmin = user.getRole().equals(Role.ADMIN);
        boolean isModerator = user.getRole().equals(Role.MODERATOR);

        if (isAuthor || isAdmin || isModerator) {
            commentRepository.delete(comment);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

}
