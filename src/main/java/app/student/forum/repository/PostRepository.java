package app.student.forum.repository;

import app.student.forum.model.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository {

    List<Post> findAll();

    Post findById(Long id);

    void save(Post post);
}
