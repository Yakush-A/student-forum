package app.student.forum.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Comment {
    @Id
    @GeneratedValue
    private Long id;

    private String text;

    @ManyToOne
    private Post post;

    @ManyToOne
    private User author;

}
