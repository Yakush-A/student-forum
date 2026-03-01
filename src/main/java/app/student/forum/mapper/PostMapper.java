package app.student.forum.mapper;

import app.student.forum.model.dto.PostDto;
import app.student.forum.model.entity.Post;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {

    public PostDto toDto(Post post) {
        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setContent(post.getContent());
        dto.setAuthor(post.getAuthor());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setEditedAt(post.getEditedAt());
        return dto;
    }
}
