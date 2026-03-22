package app.student.forum.service;

import app.student.forum.entity.*;
import app.student.forum.exception.AccessDeniedException;
import app.student.forum.exception.ErrorCode;
import app.student.forum.exception.NotFoundException;
import app.student.forum.mapper.PostMapper;
import app.student.forum.dto.post.PostDetailsResponseDto;
import app.student.forum.dto.post.PostRequestDto;
import app.student.forum.dto.post.PostResponseDto;
import app.student.forum.dto.post.PostUpdateDto;
import app.student.forum.repository.CategoryRepository;
import app.student.forum.repository.PostRepository;
import app.student.forum.repository.TagRepository;
import app.student.forum.service.cache.PostQueryKey;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.smartcardio.CardTerminal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Validated
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final PostMapper postMapper;
    private final Map<PostQueryKey, Page<PostResponseDto>> postCache = new ConcurrentHashMap<>();

    public Page<PostResponseDto> getAllPosts(Pageable pageable) {
        PostQueryKey postQueryKey = new PostQueryKey(null, null, null, pageable);

        return postCache.computeIfAbsent(
                postQueryKey,
                k -> postRepository.findAll(pageable).map(postMapper::toDto)
        );
    }

    public PostResponseDto create(@Valid PostRequestDto postRequestDto, User user) {

        Post post = new Post();

        post.setContent(postRequestDto.getContent());
        post.setAuthor(user);

        Category category = categoryRepository.findById(postRequestDto.getCategoryId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.CATEGORY_NOT_FOUND));

        post.setCategory(category);

        Set<Tag> tags = postRequestDto.getTagIds()
                .stream()
                .map(tagId -> tagRepository.findById(tagId)
                        .orElseThrow(() -> new NotFoundException(ErrorCode.TAG_NOT_FOUND)))
                .collect(Collectors.toSet());

        post.setTags(tags);

        Post savedPost = postRepository.save(post);
        postCache.clear();
        return postMapper.toDto(savedPost);
    }

    @Transactional
    public PostResponseDto patch(Long id, @Valid PostUpdateDto postUpdateDto, User user) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));

        if (!post.getAuthor().getId().equals(user.getId())) {
            throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        }

        post.setContent(postUpdateDto.getContent());

        Category category = categoryRepository.findById(postUpdateDto.getCategoryId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.CATEGORY_NOT_FOUND));

        post.setCategory(category);

        Set<Tag> tags = postUpdateDto.getTagIds()
                .stream()
                .map(tagId -> tagRepository.findById(tagId)
                        .orElseThrow(() -> new NotFoundException(ErrorCode.TAG_NOT_FOUND)))
                .collect(Collectors.toSet());

        post.setTags(tags);

        post.setEditedAt(LocalDateTime.now());

        Post updatedPost = postRepository.save(post);
        postCache.clear();
        return postMapper.toDto(updatedPost);
    }

    @Transactional
    public void deletePostById(Long id, User user) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));

        boolean isAuthor = post.getAuthor().getId().equals(user.getId());
        boolean isModerator = user.getRole().equals(Role.MODERATOR);
        boolean isAdmin = user.getRole().equals(Role.ADMIN);

        if (isAuthor || isModerator || isAdmin) {
            postRepository.delete(post);
        } else {
            throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        }
        postCache.clear();
    }

    @Transactional
    public PostDetailsResponseDto getPostById(Long id) {
        Post post = postRepository.findWithCommentsById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));
        return postMapper.toDetailsDto(post);
    }

    @Transactional
    public Page<PostResponseDto> getPostsByAuthor(Long authorId, Pageable pageable) {
        PostQueryKey postQueryKey = new PostQueryKey(authorId, null, null, pageable);

        return postCache.computeIfAbsent(
                postQueryKey,
                k -> postRepository.findByAuthorId(authorId, pageable).map(postMapper::toDto)
        );
    }

    @Transactional
    public Page<PostResponseDto> getPostsByAuthorAndCategoryName(
            Long authorId,
            String categoryName,
            Pageable pageable,
            String doNative
    ) {

        PostQueryKey postQueryKey = new PostQueryKey(null, categoryName, authorId, pageable);

        return postCache.computeIfAbsent(
                postQueryKey,
                k -> (doNative.equals("true"))
                        ? postRepository.findAllWithFiltersNative(authorId, categoryName, pageable).map(postMapper::toDto)
                        : postRepository.findAllWithFilters(authorId, categoryName, pageable).map(postMapper::toDto)
        );

    }

    @Transactional
    public List<PostResponseDto> assignUncategorizedPostsToCategory(Long categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CATEGORY_NOT_FOUND));

        List<Post> uncategorizedPosts = postRepository.findByCategoryIsNull();

        for (Post post : uncategorizedPosts) {
            if (post.valid()) {
                post.setCategory(category);
                postRepository.save(post);
            } else {
                throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
            }
        }

        return uncategorizedPosts.stream()
                .map(postMapper::toDto)
                .toList();
    }
}
