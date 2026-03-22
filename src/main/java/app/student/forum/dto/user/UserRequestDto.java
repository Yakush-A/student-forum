package app.student.forum.dto.user;

import app.student.forum.validation.ValidationConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {

    @NotBlank
    @Size(
            min = ValidationConstants.MIN_NAME_LENGTH,
            max = ValidationConstants.MAX_CONTENT_LENGTH,
            message = "{user.name.size}"
    )
    private String username;
    @NotBlank
    @Size(
            min = ValidationConstants.MIN_PASSWORD_LENGTH,
            max = ValidationConstants.MAX_PASSWORD_LENGTH,
            message = "{user.password.size"
    )
    private String password;

    @NotBlank
    @Email
    private String email;

}
