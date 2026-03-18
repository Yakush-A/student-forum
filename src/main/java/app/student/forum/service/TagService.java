package app.student.forum.service;

import app.student.forum.exception.BadRequestException;
import app.student.forum.exception.NotFoundException;
import app.student.forum.mapper.TagMapper;
import app.student.forum.model.dto.tag.TagRequestDto;
import app.student.forum.model.dto.tag.TagResponseDto;
import app.student.forum.model.entity.Tag;
import app.student.forum.repository.TagRepository;
import app.student.forum.service.cache.TagQueryKey;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;
    private final Map<TagQueryKey, Page<TagResponseDto>> tagCache = new HashMap<>();

    public TagResponseDto create(TagRequestDto tagRequestDto) {

        if (tagRepository.existsByNameIgnoreCase(tagRequestDto.getName())) {
            throw new BadRequestException("Tag already exists");
        }
        Tag tag = tagMapper.toEntity(tagRequestDto);
        Tag savedTag = tagRepository.save(tag);
        tagCache.clear();

        return tagMapper.toDto(savedTag);
    }

    public Page<TagResponseDto> findAllByName(Pageable pageable, String name) {

        TagQueryKey tagQueryKey = new TagQueryKey(name, pageable);

        return tagCache.computeIfAbsent(
                tagQueryKey,
                k -> tagRepository.findByNameContainingIgnoreCase(name, pageable).map(tagMapper::toDto)
        );
    }

    public Page<TagResponseDto> getAll(Pageable pageable) {
        TagQueryKey tagQueryKey = new TagQueryKey(null, pageable);

        return tagCache.computeIfAbsent(
                tagQueryKey,
                k -> tagRepository.findAll(pageable).map(tagMapper::toDto)
        );
    }

    public TagResponseDto getById(Long id) {

        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tag not found"));

        return tagMapper.toDto(tag);
    }

    public TagResponseDto update(Long id, TagRequestDto tagRequestDto) {

        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tag not found"));

        if (tagRequestDto.getName() == null) {
            throw new BadRequestException("Tag name is null");
        }
        if (tagRepository.existsByNameIgnoreCase(tagRequestDto.getName())) {
            throw new BadRequestException("Tag already exists");
        }
        tag.setName(tagRequestDto.getName());

        tagCache.clear();
        Tag saved = tagRepository.save(tag);

        return tagMapper.toDto(saved);
    }

    public void delete(Long id) {
        tagRepository.deleteById(id);
        tagCache.clear();
    }
}
