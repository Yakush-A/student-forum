package app.student.forum.service;

import app.student.forum.mapper.PostMapper;
import app.student.forum.model.dto.PostDto;
import app.student.forum.model.entity.Post;
import app.student.forum.repository.PostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PostService {

    private final PostRepository repository;
    private final PostMapper mapper;

    public PostService(PostRepository repository, PostMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<PostDto> getAllPosts() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public PostDto getPostById(Long id) {
        Post post = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        return mapper.toDto(post);
    }

    public List<PostDto> getPostsByAuthor(String authorUsername) {
        return repository.findByAuthorUsername(authorUsername)
                .stream()
                .map(mapper::toDto)
                .toList();
    }
}
