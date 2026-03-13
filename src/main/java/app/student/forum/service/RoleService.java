package app.student.forum.service;

import app.student.forum.mapper.RoleMapper;
import app.student.forum.model.dto.RoleRequestDto;
import app.student.forum.model.dto.RoleResponseDto;
import app.student.forum.model.entity.Role;
import app.student.forum.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleResponseDto getRole(Long id) {

        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        return roleMapper.toDto(role);
    }

    public RoleResponseDto getRoleByName(String roleName) {
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        return roleMapper.toDto(role);
    }

    public RoleResponseDto createRole(RoleRequestDto roleRequestDto) {

        Role role = roleMapper.toEntity(roleRequestDto);

        if (roleRepository.findByName(role.getName()).isPresent()) {
            throw new RuntimeException("Role already exists");
        }

        roleRepository.save(role);
        return roleMapper.toDto(role);
    }
}
