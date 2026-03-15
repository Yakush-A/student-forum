package app.student.forum.model.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeEmailDto {
    @Email
    @NotBlank
    private String email;
}
