package app.student.forum.model.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeUsernameDto {

    @NotBlank
    private String username;
}
