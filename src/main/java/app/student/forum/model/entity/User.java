package app.student.forum.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private String username;
    private String password;
    private String email;

    @OneToMany(mappedBy = "author")
    private Set<Comment> comments;

    @OneToMany(mappedBy = "author")
    private Set<Post> posts;
}
