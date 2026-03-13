package app.student.forum.mapper;

import app.student.forum.model.dto.RoleRequestDto;
import app.student.forum.model.dto.RoleResponseDto;
import app.student.forum.model.entity.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public Role toEntity(RoleRequestDto roleRequestDto) {

        Role role = new Role();

        role.setName(roleRequestDto.getRoleName());

        return role;
    }

    public RoleResponseDto toDto(Role role) {

        RoleResponseDto roleResponseDto = new RoleResponseDto();

        roleResponseDto.setRoleName(role.getName());
        roleResponseDto.setRoleId(role.getId());

        return roleResponseDto;
    }
}
