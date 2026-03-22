package app.student.forum.controller;

import app.student.forum.dto.user.*;
import app.student.forum.security.CustomUserDetails;
import app.student.forum.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public UserDetailsResponseDto getUser(@PathVariable Long id) {
        return userService.getById(id);
    }

    @GetMapping
    public Page<UserResponseDto> getAllUsers(Pageable pageable) {
        return userService.getAll(pageable);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        userService.deleteUserById(id, customUserDetails.getUser());
    }

    @PatchMapping("/email")
    public void updateEmail(
            @RequestBody ChangeEmailDto changeEmailDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        userService.changeEmail(changeEmailDto, customUserDetails.getUser());
    }

    @PatchMapping("/username")
    public void updateUsername(
            @RequestBody ChangeUsernameDto changeUsernameDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        userService.changeUsername(changeUsernameDto, customUserDetails.getUser());
    }

    @PatchMapping("/password")
    public void updatePassword(
            @RequestBody ChangePasswordDto changePasswordDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        userService.changePassword(changePasswordDto, customUserDetails.getUser());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/role")
    public void updateRole(
            @PathVariable Long id,
            @RequestBody ChangeRoleDto changeRoleDto
    ) {
        userService.changeRole(id, changeRoleDto);
    }
}
