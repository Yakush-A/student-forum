package app.student.forum.repository;

import app.student.forum.model.entity.Post;

import java.util.List;

public interface PostRepository {

    List<Post> findAll();

    Post findById(Long id);

    List<Post> findByAuthor(String author);

    void save(Post post);
}
