package app.student.forum.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
public class Post {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User author;

    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime editedAt;

    @OneToMany
    private Set<Comment> comments;

    @ManyToMany
    @JoinTable(
            name = "post_tags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;

    public Post(String content) {
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

    public Post() {
        this.createdAt = LocalDateTime.now();
    }
}
