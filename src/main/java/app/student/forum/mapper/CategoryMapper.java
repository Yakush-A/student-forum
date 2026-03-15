package app.student.forum.mapper;

import app.student.forum.model.dto.category.CategoryRequestDto;
import app.student.forum.model.dto.category.CategoryResponseDto;
import app.student.forum.model.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryMapper {

    private final PostMapper postMapper;

    public CategoryResponseDto toDto(Category category) {

        CategoryResponseDto dto = new CategoryResponseDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setPosts(category
                .getPosts()
                .stream()
                .map(postMapper::toDto)
                .toList()
        );

        return dto;
    }

    public Category toEntity(CategoryRequestDto dto) {

        Category category = new Category();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());

        return category;
    }

}
