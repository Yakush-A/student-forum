package app.student.forum.controller;

import app.student.forum.model.dto.tag.TagRequestDto;
import app.student.forum.model.dto.tag.TagResponseDto;
import app.student.forum.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<TagResponseDto> getAll() {
        return tagService.getAll();
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
}
