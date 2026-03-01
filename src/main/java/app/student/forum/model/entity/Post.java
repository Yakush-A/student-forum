package app.student.forum.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class Post {
    @Id
    private Long id;

    private String author;

    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime editedAt;

    private Set<String> topic;

    public Post(String author, String content) {
        this.author = author;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

    protected Post() {
    }

    public void edit(String newContent) {
        this.content = newContent;
        this.editedAt = LocalDateTime.now();
    }

}
