package app.student.forum.model.dto.user;

import app.student.forum.model.dto.post.PostResponseDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDetailsResponseDto {

    private Long id;
    private String username;
    private String role;

    private List<PostResponseDto> posts;

}
