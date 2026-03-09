package app.student.forum.mapper;

import app.student.forum.model.dto.UserDetailsResponseDto;
import app.student.forum.model.dto.UserRequestDto;
import app.student.forum.model.dto.UserResponseDto;
import app.student.forum.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final PostMapper postMapper;

    public UserMapper(PostMapper postMapper) {
        this.postMapper = postMapper;
    }

    public UserResponseDto toDto(User user) {

        UserResponseDto dto = new UserResponseDto();

        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole().getName());

        return dto;

    }

    public User toEntity(UserRequestDto userRequestDto) {

        User user = new User();

        user.setPassword(userRequestDto.getPassword());
        user.setEmail(userRequestDto.getEmail());
        user.setUsername(userRequestDto.getUsername());

        return user;

    }

    public UserDetailsResponseDto toDetailsDto(User user) {

        UserDetailsResponseDto dto = new UserDetailsResponseDto();

        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setPosts(user
                .getPosts()
                .stream()
                .map(postMapper::toDto)
                .toList()
        );

        return dto;

    }
}
