package app.student.forum.model.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDto {
    private String username;
    private String password;
    private String email;
}
