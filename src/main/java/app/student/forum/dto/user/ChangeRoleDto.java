package app.student.forum.dto.user;

import app.student.forum.entity.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeRoleDto {
    @NotBlank
    private Role role;
}
