package app.student.forum.service;

import app.student.forum.mapper.CommentMapper;
import app.student.forum.model.dto.CommentRequestDto;
import app.student.forum.model.dto.CommentResponseDto;
import app.student.forum.model.entity.Comment;
import app.student.forum.model.entity.Post;
import app.student.forum.model.entity.User;
import app.student.forum.repository.CommentRepository;
import app.student.forum.repository.PostRepository;
import app.student.forum.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, CommentMapper commentMapper, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public CommentResponseDto create(CommentRequestDto commentRequestDto) {

        Post post = postRepository.findById(commentRequestDto.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        User author = userRepository.findById(commentRequestDto.getAuthorId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = commentMapper.toEntity(commentRequestDto, post, author);

        var saved = commentRepository.save(comment);

        return commentMapper.toDto(saved);
    }

    public List<CommentResponseDto> getByPost(Long postId) {

        return commentRepository.findByPostId(postId)
                .stream()
                .map(commentMapper::toDto)
                .toList();
    }

    public void delete(Long id) {
        commentRepository.deleteById(id);
    }

}
