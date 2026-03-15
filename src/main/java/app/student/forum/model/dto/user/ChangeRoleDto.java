package app.student.forum.model.dto.user;

import app.student.forum.model.entity.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeRoleDto {
    @NotBlank
    private Role role;
}
