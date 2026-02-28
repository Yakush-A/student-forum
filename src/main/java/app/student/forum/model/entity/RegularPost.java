package app.student.forum.model.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class RegularPost extends PostEntity {
    private String content;
    private String title;
    private String description;

    public RegularPost(User user, PostStatus status) {
        super(user, status);
    }
}
