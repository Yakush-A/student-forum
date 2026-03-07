package app.student.forum.mapper;

import app.student.forum.model.dto.PostResponseDto;
import app.student.forum.model.entity.Post;
import app.student.forum.model.entity.Tag;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PostMapper {

    public PostResponseDto toDto(Post post) {

        PostResponseDto postResponseDto = new PostResponseDto();

        postResponseDto.setId(post.getId());
        postResponseDto.setContent(post.getContent());
        postResponseDto.setCreatedAt(post.getCreatedAt());
        postResponseDto.setEditedAt(post.getEditedAt());

        if (post.getAuthor() != null) {
            postResponseDto.setAuthorId(post.getAuthor().getId());
        }

        if (post.getCategory() != null) {
            postResponseDto.setCategoryId(post.getCategory().getId());
        }

        if (post.getTags() != null) {
            Set<Long> tagIds = post.getTags()
                    .stream()
                    .map(Tag::getId)
                    .collect(Collectors.toSet());

            postResponseDto.setTagIds(tagIds);
        }

        return postResponseDto;
    }
}
