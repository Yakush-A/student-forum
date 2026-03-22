package app.student.forum.exception;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ErrorResponse {
    private final String code;
    private final String message;
    private final int status;
    private final LocalDateTime timestamp;
    private final String path;

    public ErrorResponse(String message, String code, int status, String path) {
        this.message = message;
        this.code = code;
        this.status = status;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }
}
