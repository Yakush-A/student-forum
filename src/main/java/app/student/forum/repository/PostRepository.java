package app.student.forum.repository;

import app.student.forum.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query("""
            SELECT p FROM Post p
            LEFT JOIN FETCH p.tags
            LEFT JOIN FETCH p.category
            LEFT JOIN FETCH p.author
            """)
    Page<Post> findAll(Pageable pageable);

    @Query("""
                SELECT p FROM Post p
                LEFT JOIN FETCH p.comments
                LEFT JOIN FETCH p.tags
                LEFT JOIN FETCH p.category c
                LEFT JOIN FETCH p.author a
                WHERE (:authorId IS NULL OR a.id = :authorId)
                AND (:categoryName IS NULL OR c.name = :categoryName)
            """)
    Page<Post> findAllWithFilters(
            @Param("authorId") Long authorId,
            @Param("categoryName") String categoryName,
            Pageable pageable
    );

    @Query(value = """
                SELECT p.* FROM posts p
                JOIN users u ON p.author_id = u.id
                JOIN categories c ON p.category_id = c.id
                WHERE (:authorId IS NULL OR u.id = :authorId)
                AND (:categoryName IS NULL OR c.name = :categoryName)
            """,
            countQuery = """
                        SELECT COUNT(*)
                        FROM posts p
                        JOIN users u ON p.author_id = u.id
                        JOIN categories c ON p.category_id = c.id
                        WHERE (:authorId IS NULL OR u.id = :authorId)
                        AND (:categoryName IS NULL OR c.name = :categoryName)
                    """,
            nativeQuery = true)
    Page<Post> findAllWithFiltersNative(
            @Param("authorId") Long authorId,
            @Param("categoryName") String categoryName,
            Pageable pageable
    );

    List<Post> findByCategoryId(Long id);

    List<Post> findByCategoryIsNull();

}
