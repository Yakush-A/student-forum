package app.student.forum.service;

import app.student.forum.entity.*;
import app.student.forum.exception.AccessDeniedException;
import app.student.forum.exception.ConflictException;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
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
        log.debug("Getting all posts");
        PostQueryKey postQueryKey = new PostQueryKey(null, null, null, pageable);

        return postCache.computeIfAbsent(
                postQueryKey,
                k -> {
                    log.debug("Cache miss for posts. Loading from DB page {}", pageable);
                    return postRepository.findAll(pageable).map(postMapper::toDto);
                }
        );
    }

    public PostResponseDto create(@Valid PostRequestDto postRequestDto, User user) {
        log.info("User {} creating post", user.getId());

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
        log.debug("Post cache cleared after create");

        log.info("Post created {} by user {}", savedPost, user.getId());
        return postMapper.toDto(savedPost);
    }

    @Transactional
    public PostResponseDto patch(Long id, @Valid PostUpdateDto postUpdateDto, User user) {
        log.info("User {} patching post with id {}", user.getId(), id);

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));

        if (!post.getAuthor().getId().equals(user.getId())) {
            log.warn("Access denied! User {} is not author of post {}", user.getId(), id);
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
        log.debug("Post cache cleared after patch");

        log.info("Post patched {} by user {}", updatedPost, user.getId());
        return postMapper.toDto(updatedPost);
    }

    @Transactional
    public void deletePostById(Long id, User user) {
        log.info("User {} deleting post with id {}", user.getId(), id);

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));

        boolean isAuthor = post.getAuthor().getId().equals(user.getId());
        boolean isModerator = user.getRole().equals(Role.MODERATOR);
        boolean isAdmin = user.getRole().equals(Role.ADMIN);

        if (isAuthor || isModerator || isAdmin) {
            postRepository.delete(post);
            log.info("Post deleted {} by user {}", post, user.getId());

            log.debug("Post cache cleared after delete");
            postCache.clear();
        } else {
            log.warn("Access denied! User {} can not delete post {}", user.getId(), id);
            throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        }
    }

    @Transactional
    public PostDetailsResponseDto getPostById(Long id) {
        log.debug("Getting post with id {}", id);

        Post post = postRepository.findWithCommentsById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));
        return postMapper.toDetailsDto(post);
    }

    @Transactional
    public Page<PostResponseDto> getPostsByAuthorAndCategoryName(
            Long authorId,
            String categoryName,
            Pageable pageable,
            String doNative
    ) {
        log.info("Getting posts by author {} and category name {}", authorId, categoryName);

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
        log.info("Assigning uncategorized posts to category {}", categoryId);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CATEGORY_NOT_FOUND));

        List<Post> uncategorizedPosts = postRepository.findByCategoryIsNull();

        for (Post post : uncategorizedPosts) {
            if (post.valid()) {
                post.setCategory(category);
                postRepository.save(post);
            } else {
                log.error("Post {} is corrupted", post.getId());
                throw new ConflictException(ErrorCode.INTERNAL_SERVER_ERROR);
            }
        }

        log.info("Uncategorized posts have been assigned to category {}", categoryId);
        return uncategorizedPosts.stream()
                .map(postMapper::toDto)
                .toList();
    }
}
