package app.student.forum.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostDto {
    private Long id;
    private String content;

    private String author;

    private LocalDateTime createdAt;
    private LocalDateTime editedAt;
}
