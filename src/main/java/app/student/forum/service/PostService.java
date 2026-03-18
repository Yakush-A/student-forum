package app.student.forum.service;

import app.student.forum.exception.ForbiddenAccessException;
import app.student.forum.exception.NotFoundException;
import app.student.forum.exception.PostNotFoundException;
import app.student.forum.mapper.PostMapper;
import app.student.forum.model.dto.post.PostDetailsResponseDto;
import app.student.forum.model.dto.post.PostRequestDto;
import app.student.forum.model.dto.post.PostResponseDto;
import app.student.forum.model.dto.post.PostUpdateDto;
import app.student.forum.model.entity.*;
import app.student.forum.repository.CategoryRepository;
import app.student.forum.repository.PostRepository;
import app.student.forum.repository.TagRepository;
import app.student.forum.service.cache.PostQueryKey;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final PostMapper postMapper;
    private final Map<PostQueryKey, Page<PostResponseDto>> postCache = new ConcurrentHashMap<>();

    public Page<PostResponseDto> getAllPosts(Pageable pageable) {
        PostQueryKey postQueryKey = new PostQueryKey(null, null, pageable);

        return postCache.computeIfAbsent(
                postQueryKey,
                k -> postRepository.findAll(pageable).map(postMapper::toDto)
        );
    }

    public PostResponseDto create(PostRequestDto postRequestDto, User user) {

        Post post = new Post();

        post.setContent(postRequestDto.getContent());
        post.setAuthor(user);

        if (postRequestDto.getCategoryId() != null) {
            Category category = categoryRepository.findById(postRequestDto.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Category not found"));

            post.setCategory(category);
        } else {
            post.setCategory(null);
        }

        if (postRequestDto.getTagIds() != null && !postRequestDto.getTagIds().isEmpty()) {
            Set<Tag> tags = new HashSet<>(tagRepository.findAllById(postRequestDto.getTagIds()));
            post.setTags(tags);
        }

        Post savedPost = postRepository.save(post);
        postCache.clear();
        return postMapper.toDto(savedPost);
    }

    @Transactional
    public PostResponseDto patch(Long id, PostUpdateDto postUpdateDto, User user) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Post not found"));

        if (!post.getAuthor().getId().equals(user.getId())) {
            throw new ForbiddenAccessException("You are not allowed to patch this post");
        }

        if (postUpdateDto.getContent() != null) {
            post.setContent(postUpdateDto.getContent());
        }

        if (postUpdateDto.getCategoryId() != null) {

            Category category = categoryRepository.findById(postUpdateDto.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Category not found"));

            post.setCategory(category);
        }

        if (postUpdateDto.getTagIds() != null) {

            Set<Tag> tags = postUpdateDto.getTagIds()
                    .stream()
                    .map(tagId -> tagRepository.findById(tagId)
                            .orElseThrow(() -> new NotFoundException("Tag not found")))
                    .collect(Collectors.toSet());

            post.setTags(tags);
        }

        post.setEditedAt(LocalDateTime.now());

        Post updatedPost = postRepository.save(post);
        postCache.clear();
        return postMapper.toDto(updatedPost);
    }

    @Transactional
    public void deletePostById(Long id, User user) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Post not found"));

        boolean isAuthor = post.getAuthor().getId().equals(user.getId());
        boolean isModerator = user.getRole().equals(Role.MODERATOR);
        boolean isAdmin = user.getRole().equals(Role.ADMIN);

        if (isAuthor || isModerator || isAdmin) {
            postRepository.delete(post);
        } else {
            throw new NotFoundException("Post is corrupted");
        }
        postCache.clear();
    }

    @Transactional
    public PostDetailsResponseDto getPostById(Long id) {
        Post post = postRepository.findWithCommentsById(id)
                .orElseThrow(PostNotFoundException::new);
        return postMapper.toDetailsDto(post);
    }

    @Transactional
    public Page<PostResponseDto> getPostsByAuthor(Long authorId, Pageable pageable) {
        PostQueryKey postQueryKey = new PostQueryKey(authorId, null, pageable);

        return postCache.computeIfAbsent(
                postQueryKey,
                k -> postRepository.findByAuthorId(authorId, pageable).map(postMapper::toDto)
        );
    }

    @Transactional
    public List<PostResponseDto> assignUncategorizedPostsToCategory(Long categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category not found."));

        List<Post> uncategorizedPosts = postRepository.findByCategoryIsNull();

        for (Post post : uncategorizedPosts) {
            if (post.valid()) {
                post.setCategory(category);
                postRepository.save(post);
            } else {
                throw new ForbiddenAccessException("You are not allowed to patch this post");
            }
        }

        return uncategorizedPosts.stream()
                .map(postMapper::toDto)
                .toList();
    }
}
