package app.student.forum.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public abstract class ForumEntity {
    @Id
    private Long id;
}
