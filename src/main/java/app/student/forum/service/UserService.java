package app.student.forum.service;

import app.student.forum.model.dto.UserDto;
import app.student.forum.model.entity.User;
import app.student.forum.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    private UserDto convertToDto(User user) {
        return new UserDto(user.getName());
    }

    public UserDto findUserById(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return convertToDto(user);
    }
}
