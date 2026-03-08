package app.student.forum.mapper;

import app.student.forum.model.dto.PostDetailsResponseDto;
import app.student.forum.model.dto.PostResponseDto;
import app.student.forum.model.entity.Post;
import app.student.forum.model.entity.Tag;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {

    private final TagMapper tagMapper;
    private final CommentMapper commentMapper;

    public PostMapper(TagMapper tagMapper, CommentMapper commentMapper) {
        this.tagMapper = tagMapper;
        this.commentMapper = commentMapper;
    }

    public PostResponseDto toDto(Post post) {

        PostResponseDto dto = new PostResponseDto();

        dto.setId(post.getId());
        dto.setContent(post.getContent());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setEditedAt(post.getEditedAt());

        dto.setAuthorId(post.getAuthor().getId());

        dto.setCategoryId(post.getCategory().getId());

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
        dto.setContent(post.getContent());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setEditedAt(post.getEditedAt());

        dto.setAuthorId(post.getAuthor().getId());

        dto.setCategoryId(post.getCategory().getId());

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
