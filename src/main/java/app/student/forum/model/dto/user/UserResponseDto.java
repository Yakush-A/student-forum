package app.student.forum.model.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {

    private Long id;
    private String username;
    private String role;

}
