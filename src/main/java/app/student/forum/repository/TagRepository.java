package app.student.forum.repository;

import app.student.forum.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Page<Tag> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Tag> findAll(Pageable pageable);

    boolean existsByNameIgnoreCase(String name);
}
