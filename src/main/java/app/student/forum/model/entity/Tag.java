package app.student.forum.model.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Tag {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "tags")
    private Set<Post> posts;
}
