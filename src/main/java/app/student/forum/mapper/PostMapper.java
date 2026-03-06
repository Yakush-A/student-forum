package app.student.forum.mapper;

import app.student.forum.model.dto.PostDto;
import app.student.forum.model.entity.Post;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {

    public PostDto toDto(Post post) {
        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setAuthor(post.getAuthor().getUsername());
        dto.setContent(post.getContent());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setEditedAt(post.getEditedAt());
        dto.setTags(post.getTags().stream().toList());

        return dto;
    }
}
