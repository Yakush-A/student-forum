package app.student.forum.repository;

import app.student.forum.model.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("""
    SELECT p FROM Post p
    LEFT JOIN FETCH p.comments
    LEFT JOIN FETCH p.tags
    WHERE p.id = :id
    """)
    Optional<Post> findWithCommentsById(Long id);

    List<Post> findByAuthorId(Long id);

    List<Post> findByCategoryId(Long id);

    List<Post> findByCategoryIsNull();

}
