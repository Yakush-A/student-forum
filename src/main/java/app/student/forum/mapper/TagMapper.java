package app.student.forum.mapper;

import app.student.forum.dto.tag.TagRequestDto;
import app.student.forum.dto.tag.TagResponseDto;
import app.student.forum.entity.Tag;
import org.springframework.stereotype.Component;

@Component
public class TagMapper {

    public TagResponseDto toDto(Tag tag) {

        TagResponseDto dto = new TagResponseDto();
        dto.setName(tag.getName());
        dto.setId(tag.getId());

        return dto;
    }

    public Tag toEntity(TagRequestDto dto) {

        Tag tag = new Tag();
        tag.setName(dto.getName());

        return tag;
    }

}
