package app.student.forum.repository;

import app.student.forum.model.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("""
            SELECT p FROM Post p
            LEFT JOIN FETCH p.comments
            LEFT JOIN FETCH p.tags
            LEFT JOIN FETCH p.category
            LEFT JOIN FETCH p.author
            WHERE p.id = :id
            """)
    Optional<Post> findWithCommentsById(Long id);

    Page<Post> findByAuthorId(Long id, Pageable pageable);

    Page<Post> findAll(Pageable pageable);

    List<Post> findByCategoryId(Long id);

    List<Post> findByCategoryIsNull();

}
