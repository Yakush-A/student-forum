package app.student.forum.controller;

import app.student.forum.model.dto.UserDto;
import app.student.forum.service.UserService;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return service.findUserById(id);
    }
}
