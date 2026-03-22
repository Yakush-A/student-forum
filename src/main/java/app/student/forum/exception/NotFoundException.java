package app.student.forum.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends AppException {

    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
