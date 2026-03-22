package app.student.forum.service;

import app.student.forum.exception.ConflictException;
import app.student.forum.exception.ErrorCode;
import app.student.forum.exception.NotFoundException;
import app.student.forum.mapper.TagMapper;
import app.student.forum.dto.tag.TagRequestDto;
import app.student.forum.dto.tag.TagResponseDto;
import app.student.forum.entity.Tag;
import app.student.forum.repository.TagRepository;
import app.student.forum.service.cache.TagQueryKey;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.Map;

@Validated
@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;
    private final Map<TagQueryKey, Page<TagResponseDto>> tagCache = new HashMap<>();

    public TagResponseDto create(@Valid TagRequestDto tagRequestDto) {

        if (tagRepository.existsByNameIgnoreCase(tagRequestDto.getName())) {
            throw new ConflictException(ErrorCode.TAG_ALREADY_EXISTS);
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
                .orElseThrow(() -> new NotFoundException(ErrorCode.TAG_NOT_FOUND));

        return tagMapper.toDto(tag);
    }

    public TagResponseDto update(Long id, @Valid TagRequestDto tagRequestDto) {

        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.TAG_NOT_FOUND));

        if (tagRepository.existsByNameIgnoreCase(tagRequestDto.getName())) {
            throw new ConflictException(ErrorCode.TAG_ALREADY_EXISTS);
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
