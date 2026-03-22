package app.student.forum.controller;

import app.student.forum.dto.tag.TagRequestDto;
import app.student.forum.dto.tag.TagResponseDto;
import app.student.forum.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public TagResponseDto create(@RequestBody TagRequestDto tagRequestDto) {
        return tagService.create(tagRequestDto);
    }

    @GetMapping
    public Page<TagResponseDto> getTags(
            Pageable pageable
    ) {
        return tagService.getAll(pageable);
    }

    @GetMapping("/{id}")
    public TagResponseDto getById(@PathVariable Long id) {
        return tagService.getById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public TagResponseDto update(@PathVariable Long id, @RequestBody TagRequestDto tagRequestDto) {
        return tagService.update(id, tagRequestDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public void delete(@PathVariable Long id) {
        tagService.delete(id);
    }

    @GetMapping("/search")
    public Page<TagResponseDto> search(
            @RequestParam String name,
            Pageable pageable
    ) {
        return tagService.findAllByName(pageable, name);
    }
}
