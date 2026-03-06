package app.student.forum.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public abstract class AbstractMessage {
    private User user;
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime editedAt;
}
