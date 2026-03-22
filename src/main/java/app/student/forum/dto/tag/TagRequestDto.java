package app.student.forum.dto.tag;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagRequestDto {
    @NotNull
    @Size()
    private String name;
}
