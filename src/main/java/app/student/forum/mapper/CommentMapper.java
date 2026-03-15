package app.student.forum.mapper;

import app.student.forum.model.dto.comment.CommentResponseDto;
import app.student.forum.model.entity.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public CommentResponseDto toDto(Comment comment) {

        CommentResponseDto dto = new CommentResponseDto();

        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setPostId(comment.getPost().getId());

        if (comment.getAuthor() != null) {
            dto.setAuthorId(comment.getAuthor().getId());
        } else {
            dto.setAuthorId(null);
        }

        dto.setCreatedAt(comment.getCreatedAt());
        dto.setUpdatedAt(comment.getUpdatedAt());

        return dto;
    }
}
