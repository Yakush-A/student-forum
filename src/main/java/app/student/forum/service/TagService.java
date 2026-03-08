package app.student.forum.service;

import app.student.forum.mapper.TagMapper;
import app.student.forum.model.dto.TagRequestDto;
import app.student.forum.model.dto.TagResponseDto;
import app.student.forum.model.entity.Tag;
import app.student.forum.repository.TagRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    public TagService(TagRepository tagRepository, TagMapper tagMapper) {
        this.tagRepository = tagRepository;
        this.tagMapper = tagMapper;
    }

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
                .orElseThrow(() -> new RuntimeException("Tag not found"));

        return tagMapper.toDto(tag);
    }

    public TagResponseDto update(Long id, TagRequestDto tagRequestDto) {

        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag not found"));

        Tag updatedTag = tagMapper.toEntity(tagRequestDto);
        tag.setName(updatedTag.getName());

        Tag saved = tagRepository.save(tag);

        return tagMapper.toDto(saved);
    }

    public void delete(Long id) {
        tagRepository.deleteById(id);
    }
}
