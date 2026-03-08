package app.student.forum.controller;

import app.student.forum.model.dto.UserDetailsResponseDto;
import app.student.forum.model.dto.UserRequestDto;
import app.student.forum.model.dto.UserResponseDto;
import app.student.forum.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public UserDetailsResponseDto getUser(@PathVariable Long id) {
        return userService.getById(id);
    }

    @PostMapping
    public UserResponseDto createUser(@RequestBody UserRequestDto userRequestDto) {
        return userService.createUser(userRequestDto);
    }

    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        return userService.getAll();
    }

}
