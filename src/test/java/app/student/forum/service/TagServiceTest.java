package app.student.forum.service;

import app.student.forum.dto.tag.TagRequestDto;
import app.student.forum.dto.tag.TagResponseDto;
import app.student.forum.entity.Tag;
import app.student.forum.exception.ConflictException;
import app.student.forum.exception.NotFoundException;
import app.student.forum.mapper.TagMapper;
import app.student.forum.repository.TagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    private static final Long FIRST_TAG_ID = 1L;
    private static final int PAGE_SIZE = 10;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private TagMapper tagMapper;

    @InjectMocks
    private TagService tagService;

    @Test
    void createShouldSaveAndReturnTag() {
        TagRequestDto dto = new TagRequestDto("java");

        Tag tag = new Tag();
        tag.setName("java");

        Tag saved = new Tag();
        saved.setId(FIRST_TAG_ID);
        saved.setName("java");

        TagResponseDto response = new TagResponseDto(FIRST_TAG_ID, "java");

        when(tagRepository.existsByNameIgnoreCase("java")).thenReturn(false);
        when(tagMapper.toEntity(dto)).thenReturn(tag);
        when(tagRepository.save(tag)).thenReturn(saved);
        when(tagMapper.toDto(saved)).thenReturn(response);

        TagResponseDto result = tagService.create(dto);

        assertEquals(response, result);

        verify(tagRepository).existsByNameIgnoreCase("java");
        verify(tagMapper).toEntity(dto);
        verify(tagRepository).save(tag);
        verify(tagMapper).toDto(saved);

        verifyNoMoreInteractions(tagRepository, tagMapper);
    }

    @Test
    void createShouldThrowConflictWhenTagExists() {

        TagRequestDto dto = new TagRequestDto("java");
        when(tagRepository.existsByNameIgnoreCase("java")).thenReturn(true);

        assertThrows(ConflictException.class, () -> tagService.create(dto));

        verify(tagRepository).existsByNameIgnoreCase("java");
        verifyNoMoreInteractions(tagRepository, tagMapper);
    }

    @Test
    void createBulkShouldCreateEachTag() {

        TagRequestDto dto1 = new TagRequestDto("java");
        TagRequestDto dto2 = new TagRequestDto("spring");

        when(tagRepository.existsByNameIgnoreCase(anyString())).thenReturn(false);
        when(tagMapper.toEntity(any())).thenAnswer(inv -> {
            Tag t = new Tag();
            t.setName(((TagRequestDto) inv.getArgument(0)).getName());
            return t;
        });
        when(tagRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(tagMapper.toDto(any())).thenAnswer(inv -> {
            Tag t = inv.getArgument(0);
            return new TagResponseDto(1L, t.getName());
        });

        List<TagResponseDto> result =
                tagService.createBulk(List.of(dto1, dto2));

        assertEquals(2, result.size());
        assertEquals("java", result.get(0).getName());
        assertEquals("spring", result.get(1).getName());

        verify(tagRepository, times(2)).save(any());
    }

    @Test
    void getByIdShouldReturnTag() {

        Tag tag = new Tag();
        tag.setId(FIRST_TAG_ID);

        TagResponseDto response = new TagResponseDto(FIRST_TAG_ID, "java");

        when(tagRepository.findById(FIRST_TAG_ID)).thenReturn(Optional.of(tag));
        when(tagMapper.toDto(tag)).thenReturn(response);

        TagResponseDto result = tagService.getById(FIRST_TAG_ID);

        assertEquals(response, result);

        verify(tagRepository).findById(FIRST_TAG_ID);
        verify(tagMapper).toDto(tag);
    }

    @Test
    void getByIdShouldThrowNotFound() {

        when(tagRepository.findById(FIRST_TAG_ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> tagService.getById(FIRST_TAG_ID));

        verify(tagRepository).findById(FIRST_TAG_ID);
    }

    @Test
    void updateShouldUpdateTag() {

        Tag tag = new Tag();
        tag.setId(FIRST_TAG_ID);
        tag.setName("old");

        TagRequestDto dto = new TagRequestDto("new");

        Tag updated = new Tag();
        updated.setId(FIRST_TAG_ID);
        updated.setName("new");

        TagResponseDto response = new TagResponseDto(FIRST_TAG_ID, "new");

        when(tagRepository.findById(FIRST_TAG_ID)).thenReturn(Optional.of(tag));
        when(tagRepository.existsByNameIgnoreCase("new")).thenReturn(false);
        when(tagRepository.save(tag)).thenReturn(updated);
        when(tagMapper.toDto(updated)).thenReturn(response);

        TagResponseDto result = tagService.update(FIRST_TAG_ID, dto);

        assertEquals(response, result);
        assertEquals("new", tag.getName());

        verify(tagRepository).save(tag);
    }

    @Test
    void updateShouldThrowConflictWhenNameExists() {

        Tag tag = new Tag();
        tag.setId(FIRST_TAG_ID);

        TagRequestDto dto = new TagRequestDto("java");

        when(tagRepository.findById(FIRST_TAG_ID)).thenReturn(Optional.of(tag));
        when(tagRepository.existsByNameIgnoreCase("java")).thenReturn(true);

        assertThrows(ConflictException.class,
                () -> tagService.update(FIRST_TAG_ID, dto));
    }

    @Test
    void deleteShouldCallRepository() {

        tagService.delete(FIRST_TAG_ID);
        verify(tagRepository).deleteById(FIRST_TAG_ID);
        verifyNoMoreInteractions(tagRepository);
    }


    @Test
    void getAllShouldUseCache() {

        Pageable pageable = PageRequest.of(0, PAGE_SIZE);

        Tag tag = new Tag();
        Page<Tag> page = new PageImpl<>(List.of(tag));

        TagResponseDto dto = new TagResponseDto(1L, "java");

        when(tagRepository.findAll(pageable)).thenReturn(page);
        when(tagMapper.toDto(tag)).thenReturn(dto);

        Page<TagResponseDto> first = tagService.getAll(pageable);
        Page<TagResponseDto> second = tagService.getAll(pageable);

        assertEquals(first, second);
        verify(tagRepository, times(1)).findAll(pageable);
    }
}
