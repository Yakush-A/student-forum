package app.student.forum.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PostNotFoundException extends ResponseStatusException {
    public PostNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Post not found");
    }
}
