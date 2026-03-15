package app.student.forum.service;

import app.student.forum.mapper.TagMapper;
import app.student.forum.model.dto.tag.TagRequestDto;
import app.student.forum.model.dto.tag.TagResponseDto;
import app.student.forum.model.entity.Tag;
import app.student.forum.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    public TagResponseDto create(TagRequestDto tagRequestDto) {

        Tag tag = tagMapper.toEntity(tagRequestDto);

        Tag savedTag = tagRepository.save(tag);

        return tagMapper.toDto(savedTag);
    }

    public List<TagResponseDto> getAll() {

        return tagRepository.findAll()
                .stream()
                .map(tagMapper::toDto)
                .toList();
    }

    public TagResponseDto getById(Long id) {

        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found"));

        return tagMapper.toDto(tag);
    }

    public TagResponseDto update(Long id, TagRequestDto tagRequestDto) {

        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found"));

        if (tagRequestDto.getName() != null) {
            tag.setName(tagRequestDto.getName());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tag name is null");
        }

        Tag saved = tagRepository.save(tag);

        return tagMapper.toDto(saved);
    }

    public void delete(Long id) {
        tagRepository.deleteById(id);
    }
}
