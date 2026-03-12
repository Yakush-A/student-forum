package app.student.forum.service;

import app.student.forum.mapper.PostMapper;
import app.student.forum.model.dto.PostDetailsResponseDto;
import app.student.forum.model.dto.PostRequestDto;
import app.student.forum.model.dto.PostResponseDto;
import app.student.forum.model.dto.PostUpdateDto;
import app.student.forum.model.entity.Category;
import app.student.forum.model.entity.Post;
import app.student.forum.model.entity.Tag;
import app.student.forum.model.entity.User;
import app.student.forum.repository.CategoryRepository;
import app.student.forum.repository.PostRepository;
import app.student.forum.repository.TagRepository;
import app.student.forum.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    private final PostMapper postMapper;

    public PostService(PostRepository postRepository, UserRepository userRepository, CategoryRepository categoryRepository, TagRepository tagRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
        this.postMapper = postMapper;
    }

    public List<PostResponseDto> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::toDto)
                .toList();
    }

    public PostResponseDto create(PostRequestDto postRequestDto) {

        User author = userRepository.findById(postRequestDto.getAuthorId())
                .orElseThrow(() -> new RuntimeException("Author not found"));

        Category category = categoryRepository.findById(postRequestDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Post post = new Post();

        post.setContent(postRequestDto.getContent());
        post.setAuthor(author);
        post.setCategory(category);

        if (postRequestDto.getTagIds() != null) {

            Set<Tag> tags = postRequestDto.getTagIds()
                    .stream()
                    .map(tagId -> tagRepository.findById(tagId)
                            .orElseThrow(() -> new RuntimeException("Tag not found")))
                    .collect(Collectors.toSet());

            post.setTags(tags);
        }

        Post savedPost = postRepository.save(post);

        return postMapper.toDto(savedPost);
    }

    public PostResponseDto patch(Long id, PostUpdateDto postUpdateDto) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (postUpdateDto.getContent() != null) {
            post.setContent(postUpdateDto.getContent());
        }

        if (postUpdateDto.getCategoryId() != null) {

            Category category = categoryRepository.findById(postUpdateDto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));

            post.setCategory(category);
        }

        if (postUpdateDto.getTagIds() != null) {

            Set<Tag> tags = postUpdateDto.getTagIds()
                    .stream()
                    .map(tagId -> tagRepository.findById(tagId)
                            .orElseThrow(() -> new RuntimeException("Tag not found")))
                    .collect(Collectors.toSet());

            post.setTags(tags);
        }

        post.setEditedAt(LocalDateTime.now());

        Post updatedPost = postRepository.save(post);

        return postMapper.toDto(updatedPost);
    }

    @Transactional
    public void deletePostById(Long id) {
        postRepository.deleteById(id);
    }

    public PostDetailsResponseDto getPostById(Long id) {
        Post post = postRepository.findWithCommentsById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        return postMapper.toDetailsDto(post);
    }

    public List<PostResponseDto> getPostsByAuthor(Long authorId) {
        return postRepository.findByAuthorId(authorId)
                .stream()
                .map(postMapper::toDto)
                .toList();
    }

    @Transactional
    public List<PostResponseDto> assignUncategorizedPostsToCategory(Long categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found."));

        List<Post> uncategorizedPosts = postRepository.findByCategoryIsNull();

        for (Post post : uncategorizedPosts) {
            if (post.valid()) {
                post.setCategory(category);
                postRepository.save(post);
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
        }

        return uncategorizedPosts.stream()
                .map(postMapper::toDto)
                .toList();
    }
}
