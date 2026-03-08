package app.student.forum.controller;

import app.student.forum.model.dto.TagRequestDto;
import app.student.forum.model.dto.TagResponseDto;
import app.student.forum.service.TagService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
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
    public TagResponseDto update(@PathVariable Long id, @RequestBody TagRequestDto tagRequestDto) {
        return tagService.update(id, tagRequestDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        tagService.delete(id);
    }
}
