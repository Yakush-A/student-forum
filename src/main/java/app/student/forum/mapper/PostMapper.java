package app.student.forum.mapper;

import app.student.forum.dto.post.PostDetailsResponseDto;
import app.student.forum.dto.post.PostResponseDto;
import app.student.forum.entity.Post;
import app.student.forum.entity.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostMapper {

    private final TagMapper tagMapper;
    private final CommentMapper commentMapper;

    public PostResponseDto toDto(Post post) {

        PostResponseDto dto = new PostResponseDto();

        dto.setId(post.getId());
        dto.setContent(post.getContent());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setEditedAt(post.getEditedAt());

        dto.setAuthorId(post.getAuthor().getId());

        if (post.getCategory() != null) {
            dto.setCategoryId(post.getCategory().getId());
        } else {
            dto.setCategoryId(null);
        }

        dto.setTagIds(post
                .getTags()
                .stream()
                .map(Tag::getId)
                .toList()
        );

        return dto;
    }

    public PostDetailsResponseDto toDetailsDto(Post post) {

        PostDetailsResponseDto dto = new PostDetailsResponseDto();

        dto.setId(post.getId());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setEditedAt(post.getEditedAt());
        dto.setContent(post.getContent());

        dto.setAuthorId(post.getAuthor().getId());

        if (post.getCategory() != null) {
            dto.setCategoryId(post.getCategory().getId());
        } else {
            dto.setCategoryId(null);
        }

        dto.setTags(post
                .getTags()
                .stream()
                .map(tagMapper::toDto)
                .toList()
        );
        dto.setComments(post
                .getComments()
                .stream()
                .map(commentMapper::toDto)
                .toList()
        );

        return dto;
    }
}
