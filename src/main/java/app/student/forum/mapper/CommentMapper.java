package app.student.forum.mapper;

import app.student.forum.model.dto.CommentRequestDto;
import app.student.forum.model.dto.CommentResponseDto;
import app.student.forum.model.entity.Comment;
import app.student.forum.model.entity.Post;
import app.student.forum.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public CommentResponseDto toDto(Comment comment) {

        CommentResponseDto dto = new CommentResponseDto();

        dto.setId(comment.getId());
        dto.setContent(comment.getText());
        dto.setPostId(comment.getPost().getId());
        dto.setAuthorId(comment.getAuthor().getId());

        return dto;
    }

    public Comment toEntity(CommentRequestDto dto, Post post, User author) {

        Comment comment = new Comment();

        comment.setId(dto.getId());
        comment.setText(dto.getText());
        comment.setPost(post);
        comment.setAuthor(author);

        return comment;
    }

}
