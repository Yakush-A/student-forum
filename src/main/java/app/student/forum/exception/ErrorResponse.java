package app.student.forum.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Schema(description = "Стандартный ответ ошибки")
public class ErrorResponse {

    @Schema(description = "Код ошибки", example = "VALIDATION_ERROR")
    private final String code;

    @Schema(description = "Сообщение об ошибке", example = "Content must not be blank")
    private final String message;

    @Schema(description = "HTTP статус", example = "400")
    private final int status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(
            description = "Время ошибки",
            example = "2026-03-23 14:30:00",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private final LocalDateTime timestamp;

    @Schema(description = "Путь запроса", example = "/comments")
    private final String path;

    public ErrorResponse(String message, String code, int status, String path) {
        this.message = message;
        this.code = code;
        this.status = status;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }
}
