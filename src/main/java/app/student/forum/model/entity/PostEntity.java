package app.student.forum.model.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class PostEntity extends ForumEntity {
    private User user;
    private PostStatus status;

    public PostEntity(User user, PostStatus status) {
        this.user = user;
        this.status = status;
    }
}
