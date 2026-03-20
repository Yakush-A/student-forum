package app.student.forum.model.dto.user;

import app.student.forum.validation.ValidationConstants;
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
    private String password;
    private String email;

}
