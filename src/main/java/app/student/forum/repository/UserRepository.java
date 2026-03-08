package app.student.forum.repository;

import app.student.forum.model.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    @EntityGraph(attributePaths = "posts")
    Optional<User> findWithPostsById(Long id);

    boolean existsByUsername(String username);
}
