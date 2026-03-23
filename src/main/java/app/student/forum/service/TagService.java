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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Validated
@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;
    private final Map<TagQueryKey, Page<TagResponseDto>> tagCache = new HashMap<>();

    public TagResponseDto create(@Valid TagRequestDto tagRequestDto) {
        log.info("Creating tag {}", tagRequestDto.getName());

        if (tagRepository.existsByNameIgnoreCase(tagRequestDto.getName())) {
            throw new ConflictException(ErrorCode.TAG_ALREADY_EXISTS);
        }
        Tag tag = tagMapper.toEntity(tagRequestDto);
        Tag savedTag = tagRepository.save(tag);
        tagCache.clear();
        log.debug("Tag cache cleared after create");

        log.info("Tag created {}", savedTag.getName());
        return tagMapper.toDto(savedTag);
    }

    public Page<TagResponseDto> findAllByName(Pageable pageable, String name) {
        log.info("Finding all tags by name {}", name);

        TagQueryKey tagQueryKey = new TagQueryKey(name, pageable);

        return tagCache.computeIfAbsent(
                tagQueryKey,
                k -> {
                    log.debug("Cache miss for tags. Loading from DB with name {}", name);
                    return tagRepository.findByNameContainingIgnoreCase(name, pageable).map(tagMapper::toDto);
                }
        );
    }

    public Page<TagResponseDto> getAll(Pageable pageable) {
        log.info("Getting all tags {}", pageable);

        TagQueryKey tagQueryKey = new TagQueryKey(null, pageable);

        return tagCache.computeIfAbsent(
                tagQueryKey,
                k -> {
                    log.debug("Cache miss for tags. Loading from DB page {}", pageable);
                    return tagRepository.findAll(pageable).map(tagMapper::toDto);
                }
        );
    }

    public TagResponseDto getById(Long id) {
        log.debug("Getting tag with id {}", id);

        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.TAG_NOT_FOUND));

        return tagMapper.toDto(tag);
    }

    public TagResponseDto update(Long id, @Valid TagRequestDto tagRequestDto) {
        log.info("Updating tag {}", id);

        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.TAG_NOT_FOUND));

        if (tagRepository.existsByNameIgnoreCase(tagRequestDto.getName())) {
            throw new ConflictException(ErrorCode.TAG_ALREADY_EXISTS);
        }
        tag.setName(tagRequestDto.getName());

        Tag saved = tagRepository.save(tag);
        tagCache.clear();
        log.debug("Tag cache cleared after updating {}", saved.getName());

        log.info("Tag updated {}", saved.getId());
        return tagMapper.toDto(saved);
    }

    public void delete(Long id) {
        log.info("Deleting tag with id {}", id);

        tagRepository.deleteById(id);
        log.info("Tag deleted {}", id);
        log.debug("Tag cache cleared after deleting {}", id);
        tagCache.clear();
    }
}
