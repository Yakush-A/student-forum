package app.student.forum.repository;

import app.student.forum.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    @EntityGraph(attributePaths = {"posts", "posts.tags"})
    Optional<User> findWithPostsById(Long id);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    Page<User> findAll(Pageable pageable);
}
